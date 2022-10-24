package fr.cubibox.backroom2_5d.engine;

import fr.cubibox.backroom2_5d.Main;
import fr.cubibox.backroom2_5d.engine.collisions.LineCircle;
import fr.cubibox.backroom2_5d.engine.maths.Line2F;
import fr.cubibox.backroom2_5d.engine.maths.Point2F;
import fr.cubibox.backroom2_5d.engine.maths.Vector2F;
import fr.cubibox.backroom2_5d.engine.maths.shapes.Circle2F;
import fr.cubibox.backroom2_5d.engine.maths.shapes.Shape;
import fr.cubibox.backroom2_5d.entities.Player;
import fr.cubibox.backroom2_5d.game.Chunk;
import fr.cubibox.backroom2_5d.game.Map;
import fr.cubibox.backroom2_5d.game.MapObject;
import fr.cubibox.backroom2_5d.io.Keyboard;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

import static fr.cubibox.backroom2_5d.Main.windowHeight;
import static fr.cubibox.backroom2_5d.Main.windowWidth;
import static fr.cubibox.backroom2_5d.engine.Ray.RADIAN_PI_2;
import static fr.cubibox.backroom2_5d.utils.ImageUtils.TILE_SIZE;
import static fr.cubibox.backroom2_5d.utils.TimeUtils.ONE_SECOND_IN_NANO;
import static java.lang.Math.abs;

public class Engine implements Runnable {
    public static float screenDistance = 120.0f;
    public static float wallHeight = 16.0f;

    private final Thread engineThread = new Thread(this, "ENGINE");
    private final Player player;
    private final Map map;
    public boolean shouldStop = false;
    private int rayCount;
    private Ray [] rays;

    private boolean ready;

    public Engine(int rayCount, Player player, Map map) {
        this.rayCount = rayCount;
        this.player = player;
        this.map = map;
        this.rays = new Ray[rayCount];
    }

    @Override
    public void run() {
        while (!shouldStop) {
            this.ready = false;

            pollKeyEvents();
            updateRays();
            update(0.016f);

            this.ready = true;

            try {
                Thread.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Engine stopped !");
    }

    private void update(float dt) {
        updatePlayer();
    }

    public void pollKeyEvents() {
        Keyboard keyboard = Main.getKeyboard();
        if (keyboard.isKeyPressed(KeyEvent.VK_Z)) {
            player.getVelocity().setX((float) Math.cos(player.getAngle() * RADIAN_PI_2) * 0.5f);
            player.getVelocity().setY((float) Math.sin(player.getAngle() * RADIAN_PI_2) * 0.5f);
        } else if (keyboard.isKeyPressed(KeyEvent.VK_S)) {
            player.getVelocity().setX((float) Math.cos(player.getAngle() * RADIAN_PI_2) * -0.5f);
            player.getVelocity().setY((float) Math.sin(player.getAngle() * RADIAN_PI_2) * -0.5f);
        }

        if (!keyboard.isKeyPressed(KeyEvent.VK_S) && !keyboard.isKeyPressed(KeyEvent.VK_Z)) {
            player.setVelocity(new Vector2F(0.0f, 0.0f));
        }

        if (keyboard.isKeyPressed(KeyEvent.VK_Q)) {
            player.setAngle((player.getAngle() - 15));
        } else if (keyboard.isKeyPressed(KeyEvent.VK_D)) {
            player.setAngle((player.getAngle() + 15));
            System.out.println(player.getAngle());
        }
    }

    private void updatePlayer() {
        Chunk getPlayerChunk = map.getChunk((int) (player.getX() / 16), (int) (player.getY() / 16));

        if (getPlayerChunk != null) {
            for (Shape collisionBoxShard : player.getCollisionBox()) {
                if (collisionBoxShard instanceof Circle2F circle) {
                    for (MapObject mapObject : getPlayerChunk.getMapObjects()) {
                        for (Line2F edge : mapObject.getEdges()) {
                            Point2F nextCirclePos = new Point2F(
                                    circle.getX() + player.getX() + player.getVelocity().getX(),
                                    circle.getY() + player.getY() + player.getVelocity().getY()
                            );
                            Circle2F nextCircle = new Circle2F(nextCirclePos, circle.getRadius());

                            if (LineCircle.lineCircle(edge, nextCircle)) {
                                Point2F nextCirclePosX = new Point2F(
                                        circle.getX() + player.getX() + player.getVelocity().getX(),
                                        circle.getY() + player.getY()
                                );
                                Circle2F nextCircleX = new Circle2F(nextCirclePosX, circle.getRadius());

                                Point2F nextCirclePosY = new Point2F(
                                        circle.getX() + player.getX(),
                                        circle.getY() + player.getY() + player.getVelocity().getY()
                                );
                                Circle2F nextCircleY = new Circle2F(nextCirclePosY, circle.getRadius());

                                if (LineCircle.lineCircle(edge, nextCircleX)) {
                                    player.getVelocity().setX(0f);
                                }

                                if (LineCircle.lineCircle(edge, nextCircleY)) {
                                    player.getVelocity().setY(0f);
                                }
                            }
                        }
                    }
                }
            }
        }

        player.setX(player.getX() + player.getVelocity().getX());
        player.setY(player.getY() + player.getVelocity().getY());
    }

    private void updateRays() {
        rays = new Ray[rayCount + 1];

        int angleStep = windowWidth / rayCount;
        float halfWindowWidth = windowWidth / 2f;
        int halfHeight = windowHeight / 2;

        for (int x = 0; x <= rayCount; x++) {
            float step = x * angleStep;
            float rayAngle = ((float) Math.atan((step - halfWindowWidth) / halfWindowWidth) / RADIAN_PI_2) + player.getAngle();

            Ray r = new Ray(player.getX(), player.getY(), rayAngle);
            updateRay(r);

            for (float y = 1; y < halfHeight; y++) {
                float directDistFloor = (screenDistance * halfHeight) / (int) (y);
                float realDistFloor = (float) (directDistFloor / Math.cos((player.getAngle() - r.getAngle()) * RADIAN_PI_2));

                float floorX = (float) (player.getX() + Math.cos(r.getAngle() * RADIAN_PI_2) * realDistFloor / (screenDistance / 2f));
                float floorY = (float) (player.getY() + Math.sin(r.getAngle() * RADIAN_PI_2) * realDistFloor / (screenDistance / 2f));

                r.addFloorHit(new Point2F(floorX, floorY));
            }

            rays[x] = r;
        }
    }

    private ArrayList<Chunk> findTraveledChunk(Ray r) {
        ArrayList<Chunk> chunksFound = new ArrayList<>();
        Chunk[][] chunks = this.getMap().getChunks();

        for (Chunk[] LineChunk : chunks)
            Collections.addAll(chunksFound, LineChunk);

        return chunksFound;
    }

    private void getIntersectEdge(Ray r, ArrayList<Chunk> chunks) {
        float dRay = Float.MAX_VALUE;
        float tempX = r.getIntersectionX();
        float tempY = r.getIntersectionY();

        int textureID = 0;

        for (Chunk chunk : chunks) {
            if (chunk != null) {
                for (MapObject mapObject : chunk.getMapObjects()) {
                    for (Line2F edge : mapObject.getEdges()) {
                        float p1X = r.getStartX();
                        float p1Y = r.getStartY();
                        float p2X = r.getIntersectionX();
                        float p2Y = r.getIntersectionY();

                        float p3X = edge.getA().getX();
                        float p3Y = edge.getA().getY();
                        float p4X = edge.getB().getX();
                        float p4Y = edge.getB().getY();

                        float s1_x, s1_y, s2_x, s2_y;
                        s1_x = p2X - p1X;
                        s1_y = p2Y - p1Y;
                        s2_x = p4X - p3X;
                        s2_y = p4Y - p3Y;

                        float s, t;
                        float v = -s2_x * s1_y + s1_x * s2_y;
                        s = (-s1_y * (p1X - p3X) + s1_x * (p1Y - p3Y)) / v;
                        t = (s2_x * (p1Y - p3Y) - s2_y * (p1X - p3X)) / v;

                        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
                            float intx = p1X + (t * s1_x);
                            float inty = p1Y + (t * s1_y);

                            float tempdRay = computeSquareRayDistance(r, intx, inty);
                            if (tempdRay < dRay) {
                                dRay = tempdRay;
                                tempX = intx;
                                tempY = inty;

                                float dx = tempX - p3X;
                                float dy = tempY - p3Y;

                                textureID = (int) ((abs(dx) + abs(dy)) * (TILE_SIZE / 2)) % TILE_SIZE;
                            }
                        }
                    }
                }
            }
        }

        r.setTextureIndex(textureID);
        r.setIntersectionX(tempX);
        r.setIntersectionY(tempY);
    }

    /**
     * actualise la distance entre la line et le joueur du Rayon (DRay)
     * actualise egalement le intersectionPoint du Rayon
     */
    private float computeSquareRayDistance(Ray r, float x, float y) {
        float x1 = r.getStartX();
        float y1 = r.getStartY();

        float dx = x1 - x;
        float dy = y1 - y;

        return dx * dx + dy * dy;
    }

    private void updateRay(Ray r) {
        ArrayList<Chunk> chunks = findTraveledChunk(r);
        getIntersectEdge(r, chunks);
    }

    public void start() {
        engineThread.start();
    }

    public void stop() {
        shouldStop = true;
    }

    public Map getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

    public Ray [] getRays() {
        return rays;
    }

    public float getRayCount() {
        return rayCount;
    }

    public void setRayCount(int doubleValue) {
        this.rayCount = doubleValue;
    }

    public boolean isReady() {
        return ready;
    }
}
