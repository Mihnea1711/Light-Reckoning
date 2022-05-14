package com.Game;

import com.Utilities.Pair;

/**
 * Every engine needs a camera!
 * Important!
 */
public class Camera {
    public Pair pos; //position of camera

    /**
     * Constructor.
     * @param pos position of camera
     */
    public Camera(Pair pos) {
        this.pos = pos;
    }

    /**
     * Helper method.
     * @return camera x position
     */
    public float getPosX() {
        return this.pos.x;
    }

    /**
     * Helper method.
     * @return camera y position
     */
    public float getPosY() {
        return this.pos.y;
    }
}
