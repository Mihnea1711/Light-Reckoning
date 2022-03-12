package com.Components;

import com.File.Parser;
import com.Game.Component;
import com.Utilities.TwoPair;

public class BoxBounds extends Bounds {
    public float width, height;
    public float halfWidth, halfHeight;     //calculate them once, so we don't waste cpu calculating everytime
    public TwoPair centre = new TwoPair();                  //we will change the obj every frame

    public BoxBounds (float width, float height){
        this.width = width;
        this.height = height;
        this.halfHeight = height / 2;
        this.halfWidth = width / 2;
        this.type = BoundsType.Box;
    }

    @Override
    public void start() {           //the boxBounds is attached to the gameobject
        this.calculateCentre();
    }

    public void calculateCentre() {
        this.centre.x = this.gameObject.transform.pos.x + this.halfWidth;
        this.centre.y = this.gameObject.transform.pos.y + this.halfHeight;
    }

    @Override
    public void update(double dTime) {

    }

    public static boolean checkCollision(BoxBounds b1, BoxBounds b2) {
        b1.calculateCentre();
        b2.calculateCentre();

        float dx = b2.centre.x - b1.centre.x;
        float dy = b2.centre.y - b1.centre.y;

        float combinedHalfWidths = b1.halfWidth + b2.halfWidth;
        float combinedHalfHeights = b1.halfHeight + b2.halfHeight;

        if(Math.abs(dx) <= combinedHalfWidths) {            //if they are colliding on the x axis
            return Math.abs(dy) < combinedHalfHeights;          //return whether or not they collide on the y axis
        }
        return false;       //if not colliding
    }

    @Override
    public Component copy() {
        return new BoxBounds(width, height);
    }

    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("BoxBounds", tabSize));
        builder.append(addFloatProperty("Width", this.width, tabSize + 1, true, true));
        builder.append(addFloatProperty("Height", this.height, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    public static BoxBounds deserialize() {
        float width = Parser.consumeFloatProperty("Width");
        Parser.consume(',');
        float height = Parser.consumeFloatProperty("Height");
        Parser.consumeEndObjectProperty();

        return new BoxBounds(width, height);
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }
}
