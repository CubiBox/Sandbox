package fr.cubibox.backroom2_5d.game;

import fr.cubibox.backroom2_5d.engine.maths.Vector2F;

public class Camera {
    private final Vector2F position;
    private float scale = 1F;

    public Camera(float x, float y) {
        this.position = new Vector2F(x, y);
    }

    public Vector2F getPosition() {
        return position;
    }

    public float getPosX() {
        return position.getX();
    }

    public void setPosX(float posX) {
        this.position.setX(posX);
    }

    public float getPosY() {
        return position.getY();
    }

    public void setPosY(float posY) {
        this.position.setY(posY);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}