package fr.cubibox.sandbox;

import fr.cubibox.sandbox.engine.GameEngine;
import fr.cubibox.sandbox.engine.io.Keyboard;
import fr.cubibox.sandbox.engine.io.Mouse;

public class Main {
    // TODO: Move theses functions to another class
    public static final Keyboard keyboard = new Keyboard();
    public static final Mouse mouse = new Mouse();
    public static final float RADIAN_PI_2 = 0.0174532925199f;
    public static int windowWidth = 720;
    public static int windowHeight = 480;

    public static void main(String[] args) {
        GameEngine.getInstance().start();
    }
}