package fr.cubibox.backroom2_5d.game;

import java.util.ArrayList;

public class Chunk {
    public static final int CHUNK_SIZE = 16;
    private ArrayList<Polygon> pols = new ArrayList<>();
    private boolean isLoad;

    // TODO
    private int originX, originY;

    public Chunk(ArrayList<Polygon> pols, int x, int y) {
        this.pols = pols;
        this.isLoad = false;
        this.originX = x;
        this.originY = y;
    }

    public ArrayList<Polygon> getPols() {
        return pols;
    }

    public void setPols(ArrayList<Polygon> pols) {
        this.pols = pols;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public int getOriginX() {
        return originX;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }
}
