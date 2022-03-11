package com.DataStructures;

import com.File.Serialize;
import com.Utilities.TwoPair;

public class Transform extends Serialize {
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

    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("Transform", tabSize));
        builder.append(beginObjectProperty("Position", tabSize + 1));
        builder.append(pos.serialize(tabSize + 2));
        builder.append(closeObjectProperty(tabSize + 1));
        builder.append(addEnding(true, true));

        builder.append(beginObjectProperty("Scale", tabSize + 1));
        builder.append(scale.serialize(tabSize + 2));
        builder.append(closeObjectProperty(tabSize + 1));
        builder.append(addEnding(true, true));

        builder.append(addFloatProperty("Rotation", rotation, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();

    }
}



