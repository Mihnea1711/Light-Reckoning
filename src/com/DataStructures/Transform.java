package com.DataStructures;

import com.Utilities.TwoPair;

public class Transform {
    public TwoPair pos;
    public TwoPair scale;
    public float rotation;

    public Transform(TwoPair pos) {
        this.pos = pos;
        this.scale = new TwoPair(1.0f, 1.0f);
        this.rotation = 0.0f;
    }

    //format the print of a transform
    @Override
    public String toString() {
        return "Position (" + pos.x + ", " + pos.y + ")";
    }

    public Transform copy() {
        Transform transform = new Transform(this.pos.copy());
        transform.scale = this.scale.copy();
        transform.rotation = this.rotation;
        return transform;
    }
}



