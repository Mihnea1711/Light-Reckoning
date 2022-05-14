package com.DataStructures;

import com.File.Parser;
import com.File.Serialize;
import com.Utilities.Pair;

/**
 * Holds information about of the object.
 */
public class Transform extends Serialize {
    public Pair pos;
    public Pair scale;
    public float rotation;

    /**
     * Constructor.
     * @param pos object position
     */
    public Transform(Pair pos) {
        this.pos = pos;
        this.scale = new Pair(1.0f, 1.0f);       //no rescaling
        this.rotation = 0.0f;                   //default rotation
    }

    /**
     * Constructor.
     * @param pos object position
     * @param scale object scale
     * @param rotation  object rotation
     */
    public Transform(Pair pos, Pair scale, float rotation) {
        this.pos = pos;
        this.scale = scale;
        this.rotation = rotation;
    }

    /**
     * Format the print of a transform.
     * @return the transform as a string
     */
    @Override
    public String toString() {
        return "Position (" + pos.x + ", " + pos.y + ")";
    }

    /**
     * Creates a new object with the same properties, instead of passing a reference around.
     * @return new object = copy of a transform
     */
    public Transform copy() {
        Transform transform = new Transform(this.pos.copy());
        transform.scale = this.scale.copy();
        transform.rotation = this.rotation;
        return transform;
    }

    /**
     * Serializes the transform.
     * @param tabSize   number of tabs to be indented correctly
     * @return the transform as a string
     */
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

    /**
     * Deserializes the transform.
     * @return a new Transform object with the deserialized properties.
     */
    public static Transform deserialize() {
        Parser.consumeBeginObjectProperty("Transform");
        Parser.consumeBeginObjectProperty("Position");
        Pair pos = Pair.deserialize();
        Parser.consumeEndObjectProperty();
        Parser.consume(',');

        Parser.consumeBeginObjectProperty("Scale");
        Pair scale = Pair.deserialize();
        Parser.consumeEndObjectProperty();
        Parser.consume(',');

        float rotation = Parser.consumeFloatProperty("Rotation");
        Parser.consumeEndObjectProperty();

        Transform t = new Transform(pos, scale, rotation);

        return t;
    }
}



