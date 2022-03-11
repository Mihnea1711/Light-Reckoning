package com.Utilities;

import com.File.Serialize;

//(x, y)
public class TwoPair extends Serialize {
    public float x, y;

    public TwoPair(float x, float y){
        this.x = x;
        this.y = y;
    }

    public TwoPair() {
        this.x = 0;
        this.y = 0;
    }

    public TwoPair copy() {
        return new TwoPair(this.x, this.y);
    }

    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(addFloatProperty("x", x, tabSize, true, true));
        builder.append(addFloatProperty("y", y, tabSize, true, false));

        return builder.toString();
    }
}
