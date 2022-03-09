package com.DataStructures;

import com.Utilities.TwoPair;

public class Transform {
    public TwoPair pos;
    public TwoPair scale;
    public TwoPair rotation;

    public Transform(TwoPair pos) {
        this.pos = pos;
    }

    //format the print of a transform
    @Override
    public String toString() {
        return "Position (" + pos.x + ", " + pos.y + ")";
    }
}

