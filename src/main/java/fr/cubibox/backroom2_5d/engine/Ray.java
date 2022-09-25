package fr.cubibox.backroom2_5d.engine;

import fr.cubibox.backroom2_5d.engine.maths.Point2F;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Classe de définition de rayon pour le raycasting
 */
public class Ray {
    public static final float RADIAN_PI_2 = 0.0174532925199f;
    private static final float dist = 32f;

    private final float angle;
    private final Point2F startPoint;
    private final Point2F intersectionPoint;

    private float squareDistance;

    /**
     * @param startPoint
     * @param angle      Crée un rayon à partir d'un point de départ et d'un angle
     */
    public Ray(Point2F startPoint, float angle) {
        this.squareDistance = 32f * 32f;
        this.startPoint = startPoint;
        this.angle = angle;

        this.intersectionPoint = new Point2F(
                (float) (cos(angle * RADIAN_PI_2) * dist) + startPoint.getX(),
                (float) (sin(angle * RADIAN_PI_2) * dist) + startPoint.getY()
        );
    }

    /**
     * @param x
     * @param y
     * @param angle Crée un rayon à partir d'une coordonée x et y, puis d'un angle
     */
    public Ray(float x, float y, float angle) {
        this.squareDistance = 32f * 32f;
        this.startPoint = new Point2F(x, y);
        this.angle = angle;

        //set the intersection point with a length of 32

        this.intersectionPoint = new Point2F(
                (float) (cos(angle * RADIAN_PI_2) * dist) + startPoint.getX(),
                (float) (sin(angle * RADIAN_PI_2) * dist) + startPoint.getY()
        );
    }

    /**
     * @return Retourne l'angle du rayon
     */
    public float getAngle() {
        return angle;
    }

    public float getStartX() {
        return startPoint.getX();
    }

    public float getStartY() {
        return startPoint.getY();
    }

    public Point2F getStartPoint() {
        return startPoint;
    }

    public float getIntersectionX() {
        return intersectionPoint.getX();
    }

    public void setIntersectionX(float x) {
        intersectionPoint.setX(x);
    }

    public float getIntersectionY() {
        return intersectionPoint.getY();
    }

    public void setIntersectionY(float y) {
        intersectionPoint.setY(y);
    }

    public float getSquareDistance() {
        return squareDistance;
    }

    public void setSquareDistance(float squareDistance) {
        this.squareDistance = squareDistance;
    }
}
