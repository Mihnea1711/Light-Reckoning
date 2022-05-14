package com.Utilities;

import com.File.Parser;
import com.File.Serialize;

/**
 * Encapsulates a pair of numbers (for position/ scale/ ...).
 * (x, y)
 */
public class Pair extends Serialize {
    public float x, y;

    /**
     * Constructor with parameters.
     * @param x x pos
     * @param y y pos
     */
    public Pair(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * Default constructor.
     * initializes pos with 0,0
     */
    public Pair() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Creates a new object, instead of passing a reference around.
     * @return new object = copy of a Pair
     */
    public Pair copy() {
        return new Pair(this.x, this.y);
    }

    /**
     * Serializes the pair of numbers.
     * @param tabSize   number of tabs to be indented correctly
     * @return the two numbers as a string to be added.
     */
    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(addFloatProperty("x", x, tabSize, true, true));
        builder.append(addFloatProperty("y", y, tabSize, true, false));

        return builder.toString();
    }

    /**
     * Deserializes the pair of values.
     * @return  the pair deserialized as a new Pair object
     */
    public static Pair deserialize() {
        float x = Parser.consumeFloatProperty("x");
        Parser.consume(',');
        float y = Parser.consumeFloatProperty("y");

        return new Pair(x, y);
    }
}
