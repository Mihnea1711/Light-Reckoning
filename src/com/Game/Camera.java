package com.Game;

import com.Utilities.TwoPair;

public class Camera {
    public TwoPair pos;

    public Camera(TwoPair pos) {
        this.pos = pos;
    }

    public float getPosX() {
        return this.pos.x;
    }

    public float getPosY() {
        return this.pos.y;
    }
}
