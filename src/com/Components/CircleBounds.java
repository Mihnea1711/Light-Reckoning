package com.Components;

import com.File.Parser;
import com.Game.Component;
import com.Game.GameObject;
import com.Utilities.Constants;
import com.Utilities.Pair;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class CircleBounds extends Bounds{
    private float diameter, radius;
    private Pair centre = new Pair();

    public float xBuffer = 0.0f;
    public float yBuffer = 0.0f;

    public boolean isVisible;

    public CircleBounds(float diameter, boolean isVisible) {
        this.diameter = diameter;
        this.radius = diameter / 2.0f;
        this.type = BoundsType.Circle;
        this.isVisible = isVisible;
    }

    public void start() {
        this.calculateCentre();
    }

    public void calculateCentre() {
        this.centre.x = this.gameObject.transform.pos.x + this.radius + this.xBuffer;
        this.centre.y = this.gameObject.transform.pos.y + this.radius + this.yBuffer;
    }

    /**
     * Abstract because we have different scenarios (Box, Triangle)
     *
     * @return the width
     */
    @Override
    public float getWidth() {
        return diameter;
    }

    /**
     * Abstract because we have different scenarios (Box, Triangle)
     *
     * @return the height
     */
    @Override
    public float getHeight() {
        return diameter;
    }

    /**
     * Function to check if a point is within some bounds.
     *
     * @param pos position of the mouse
     * @return true/false
     */
    @Override
    public boolean rayCast(Pair pos) {
        return pos.x > this.gameObject.getPosX() + xBuffer && Math.abs(pos.x - this.centre.x) < this.radius &&
                pos.y > this.gameObject.getPosY() + yBuffer && Math.abs(pos.y - this.centre.y) < this.radius;
    }

    /**
     * The method which all the components extending this class will have to override.
     *
     * @param tabSize number of tabs to be indented correctly
     * @return the string to be written into the file
     */
    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("CircleBounds", tabSize));
        builder.append(addFloatProperty("Diameter", this.diameter, tabSize + 1, true, true));
        builder.append(addFloatProperty("xBuffer", this.xBuffer, tabSize + 1, true, true));
        builder.append(addFloatProperty("yBuffer", this.yBuffer, tabSize + 1, true, true));
        builder.append(addBooleanProperty("isVisible", this.isVisible, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    public static CircleBounds deserialize() {
        float diameter = Parser.consumeFloatProperty("Diameter");
        Parser.consume(',');
        float xBuffer = Parser.consumeFloatProperty("xBuffer");
        Parser.consume(',');
        float yBuffer = Parser.consumeFloatProperty("yBuffer");
        Parser.consume(',');
        boolean isVisible = Parser.consumeBooleanProperty("isVisible");
        Parser.consumeEndObjectProperty();

        CircleBounds bounds = new CircleBounds(diameter, isVisible);
        bounds.xBuffer = xBuffer;
        bounds.yBuffer = yBuffer;
        return bounds;
    }

    public static boolean checkCollision(BoxBounds b, CircleBounds c) {
        b.calculateCentre();
        c.calculateCentre();

        float dx = c.centre.x - b.centre.x;
        float dy = c.centre.y - b.centre.y;

        float combinedHalfWidths = b.halfWidth + c.radius;
        float combinedHalfHeights = b.halfHeight + c.radius;

        if (Math.abs(dx) <= combinedHalfWidths) {            //if they are colliding on the x-axis
            return Math.abs(dy) <= combinedHalfHeights;          //return whether they collide on the y-axis or not
        }
        return false;
    }

    public void resolveCollision(GameObject player) {
        if(this.isVisible) {
            player.getComp(Player.class).increaseCoinsCollected();
            this.isVisible = false;
        }
    }

    /**
     * Abstract method to force every derived class to implement it
     *
     * @return a new object = copy of a Component
     */
    @Override
    public Component copy() {
        CircleBounds bounds = new CircleBounds(diameter, this.isVisible);
        bounds.xBuffer = xBuffer;
        bounds.yBuffer = yBuffer;
        return bounds;
    }

    @Override
    public void update(double dTime) {
    }

    @Override
    public void draw(Graphics2D g2) {
        if(isSelected) {
            g2.setColor(Color.GREEN);
            //g2.setStroke(Constants.ThickLine);
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{15}, 0));
            Shape circle = new Ellipse2D.Float(this.gameObject.getPosX() + xBuffer, this.gameObject.getPosY() + yBuffer, this.diameter, this.diameter);
            g2.draw(circle);
            g2.setStroke(Constants.Line);
        }
    }
}
