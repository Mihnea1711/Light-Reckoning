package com.Components;

import com.File.Parser;
import com.Game.Component;
import com.Game.Window;
import com.Utilities.Constants;
import com.Utilities.Pair;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * Class for the spikes' bounds
 */
public class TriangleBounds extends Bounds{
    private float base, height, halfWidth, halfHeight;

    /**
     * circumscribed circle radius
     */
    private float enclosingRadius;

    private float x1, x2, x3, y1, y2, y3;   //points of the triangle

    public float xBuffer, yBuffer;

    //will work with binary codes
    /**
     * binary code for the "inside" zone
     */
    private final int Inside = 0;

    /**
     * binary code for the "left" zone
     */
    private final int Left = 1;

    /**
     * binary code for the "right" zone
     */
    private final int Right = 2;

    /**
     * binary code for the "bottom" zone
     */
    private final int Bottom = 4;

    /**
     * binary code for the "top" zone
     */
    private final int Top = 8;

    /**
     * Constructor.
     * @param base value for the base of the triangle
     * @param height value for the height of the triangle
     */
    public TriangleBounds(float base, float height) {
        this.type = BoundsType.Triangle;
        this.base = base;
        this.height = height;
        this.halfHeight = this.height / 2.0f;
        this.halfWidth = this.base / 2.0f;
        this.enclosingRadius = Math.max(this.halfHeight, this.halfWidth);
    }

    /**
     * Gets called after the game object is created.
     */
    @Override
    public void start() {
        calculateTransform();
    }

    /**
     * Assigns all the appropriate values to x1,2,3 , y1,2,3
     */
    public void calculateTransform() {
        double rAngle = Math.toRadians(gameObject.getRotation());
        Pair p1 = new Pair(gameObject.getPosX() + xBuffer, gameObject.getPosY() + yBuffer + height);
        Pair p2 = new Pair(gameObject.getPosX() + xBuffer + halfWidth, gameObject.getPosY() + yBuffer);
        Pair p3 = new Pair(gameObject.getPosX() + xBuffer + base, gameObject.getPosY() + yBuffer + height);
        Pair origin = new Pair(gameObject.getPosX() + xBuffer + (Constants.TileWidth / 2.0f),
                gameObject.getPosY() + yBuffer + (Constants.TileHeight / 2.0f));

        p1 = rotatePoint(rAngle, p1, origin);
        p2 = rotatePoint(rAngle, p2, origin);
        p3 = rotatePoint(rAngle, p3, origin);

        this.x1 = p1.x;
        this.y1 = p1.y;
        this.x2 = p2.x;
        this.y2 = p2.y;
        this.x3 = p3.x;
        this.y3 = p3.y;
    }

    /**
     * Checks the collision with a triangle.
     * @param b the player
     * @param t the spike
     * @return Goes into the narrow phase and checks whether we hit the spike or not
     */
    public static boolean checkCollision(BoxBounds b, TriangleBounds t) {
        if(t.broadPhase(b)) {
            return t.narrowPhase(b);
        }
        return false;
    }

    /**
     * Checks if the player hit the enclosing radius of a triangle.
     * First step in collision checking.
     * @param b player
     * @return true/false
     */
    private boolean broadPhase(BoxBounds b) {
        //circle collision
        float bRadius = b.enclosingRadius;
        float tRadius = this.enclosingRadius;

        float centreX = this.x2;
        float centreY = this.y2 + halfHeight;

        float playerCentreX = b.gameObject.getPosX() + b.halfWidth;
        float playerCentreY = b.gameObject.getPosY() + b.halfHeight;

        //dist between the player and triangle
        Pair dist = new Pair(playerCentreX - centreX, playerCentreY - centreY);
        float magSquared = (dist.x * dist.x) + (dist.y * dist.y);
        float radiSquared = (bRadius + tRadius) * (bRadius + tRadius);

        return magSquared <= radiSquared;
    }

    /**
     * Rotates the point around an origin.
     * @param angle angle of rotation
     * @param p the point to be rotated
     * @param o the origin which the point is being rotated around
     * @return the new rotated point
     */
    private Pair rotatePoint(double angle, Pair p, Pair o) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        Pair newPair = new Pair(p.x, p.y);

        newPair.x -= o.x;
        newPair.y -= o.y;

        float newX = (float)((newPair.x * cos) - (newPair.y * sin));
        float newY = (float)((newPair.x * sin) + (newPair.y * cos));

        return new Pair(newX + o.x, newY + o.y);
    }

    /**
     * Computes the region we are in.
     * @param point the point to be checked
     * @param bounds bounds of player
     * @return the correct region code
     */
    private int computeRegionCode(Pair point, BoxBounds bounds) {
        int code = Inside;
        Pair topLeft = bounds.gameObject.transform.pos;

        //check if the point is to the left or to the right of bounds
        if(point.x < topLeft.x) {
            code |= Left;
        } else if (point.x > topLeft.x + bounds.getWidth()) {
            code |= Right;
        }

        //check if the point is below or above
        if(point.y < topLeft.y) {
            code |= Top;
        } else if(point.y > topLeft.y + bounds.getHeight()) {
            code |= Bottom;
        }

        return code;
    }

    /**
     * Cohen Sutherland clipping algorithm. Should run 2 times at most.
     * @param p1 first end point of the line
     * @param p2  second end point of the line
     * @param depth recursive functions need a maximum amount of loops. (catch)
     * @param bounds the actual bounds of the player
     * @param pos the position of the boxBounds
     * @return true/false whether we are intersecting or not
     */
    private boolean boxIntersectingLine(Pair p1, Pair p2, int depth, BoxBounds bounds, Pair pos) {
        if(depth > 5) {
            System.out.println("Max depth exceeded");
            return true;
        }
        int code1 = computeRegionCode(p1, bounds);
        int code2 = computeRegionCode(p2, bounds);

        //check if line is completely inside/outside/half in/half out
        if(code1 == 0 && code2 == 0) {
            //line inside
            return true;
        } else if ((code1 & code2) != 0) {
            //line completely out
            return false;
        } else if(code1 == 0 || code2 == 0) {
            //intersecting (1 point inside, 1 point outside)
            return true;
        }

        int xmax = (int)(pos.x + bounds.getWidth());
        int xmin = (int)pos.x;

        //y = mx + b
        float m = (p2.y - p1.y) / (p2.x - p1.x);
        float b = p2.y - (m * p2.x);

        if((code1 & Left) == Left) {
            //add 1 to ensure we're inside clipping polygon
            p1.x = xmin + 1;
        } else if((code1 & Right) == Right) {
            //subtract 1 (same reason
            p1.x = xmax - 1;
        }
        p1.y = (m * p1.x) + b;

        //now repeat for p2
        if((code2 & Left) == Left) {
            //add 1 to ensure we re inside clipping polygon
            p2.x = xmin + 1;
        } else if((code2 & Right) == Right) {
            //subtract 1
            p2.x = xmax - 1;
        }
        p2.y = (m * p2.x) + b;

        return boxIntersectingLine(p1, p2, depth + 1, bounds, pos);
    }

    /**
     * Second phase of collision resolution. Checks if the player has collided with the actual bounds of the triangle.
     * @param b player bounds
     * @return true/false
     */
    private boolean narrowPhase(BoxBounds b) {
        Pair p1 = new Pair(x1, y1);
        Pair p2 = new Pair(x2, y2);
        Pair p3 = new Pair(x3, y3);

        //origin = centre of box bounds
        Pair origin = new Pair(b.gameObject.getPosX() + (b.getWidth() / 2.0f), b.gameObject.getPosY() + (b.getHeight() / 2.0f));

        float rAngle = (float)Math.toRadians(b.gameObject.getRotation());

        p1 = rotatePoint(rAngle, p1, origin);
        p2 = rotatePoint(rAngle, p2, origin);
        p3 = rotatePoint(rAngle, p3, origin);

        return (boxIntersectingLine(p1, p2, 0, b, b.gameObject.transform.pos) ||
                boxIntersectingLine(p2, p3, 0, b, b.gameObject.transform.pos) ||
                boxIntersectingLine(p3, p1, 0, b, b.gameObject.transform.pos));
    }

    /**
     * Computes scalar product/angle of 2 vectors.
     * @param v1 first vector
     * @param v2 second vector
     * @return a scalar - algebraically (angle between the vectors - geometrically)
     */
    public float dot(Pair v1, Pair v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    /**
     * Checks if a point is inside a triangle bounds.
     * @param pos position of point
     * @return true/false
     */
    @Override
    public boolean rayCast(Pair pos) {
        //Compute Vectors
        Pair v0 = new Pair(x3 - x1, y3 - y1);
        Pair v1 = new Pair(x2 - x1, y2 - y1);
        Pair v2 = new Pair(pos.x - x1, pos.y - y1);

        //Compute dot products
        float dot00 = dot(v0, v0);
        float dot01 = dot(v0, v1);
        float dot02 = dot(v0, v2);
        float dot11 = dot(v1, v1);
        float dot12 = dot(v1, v2);

        //Compute barycentric coordinates
        float invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
        float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        return (u >= 0.0f) && (v >= 0.0f) && (u + v < 1.0f);
    }

    /**
     * Utility method.
     * @return the width of the triangle
     */
    @Override
    public float getWidth() {
        return this.base;
    }

    /**
     * Utility method.
     * @return the height of the triangle
     */
    @Override
    public float getHeight() {
        return this.height;
    }

    /**
     * Draws the bounds of the triangle selected in relation with world coordinates.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        if(isSelected) {
            g2.setStroke(Constants.ThickLine);
            g2.setColor(Color.GREEN);
            g2.draw(new Line2D.Float(
                    this.x1 + xBuffer - Window.getWindowCamX(),
                    this.y1 + yBuffer - Window.getWindowCamY(),
                    this.x2 + xBuffer - Window.getWindowCamX(),
                    this.y2 + yBuffer - Window.getWindowCamY()));
            g2.draw(new Line2D.Float(
                    this.x1 + xBuffer - Window.getWindowCamX(),
                    this.y1 + yBuffer - Window.getWindowCamY(),
                    this.x3 + xBuffer - Window.getWindowCamX(),
                    this.y3 + yBuffer - Window.getWindowCamY()));
            g2.draw(new Line2D.Float(
                    this.x3 + xBuffer - Window.getWindowCamX(),
                    this.y3 + yBuffer - Window.getWindowCamY(),
                    this.x2 + xBuffer - Window.getWindowCamX(),
                    this.y2 + yBuffer - Window.getWindowCamY()));
            g2.setStroke(Constants.Line);
        }
    }

    /**
     * Serializes the triangle bounds.
     * @param tabSize number of tabs to be indented correctly
     * @return the bounds as a string
     */
    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("TriangleBounds", tabSize));
        builder.append(addFloatProperty("Base", this.base, tabSize + 1, true, true));
        builder.append(addFloatProperty("Height", this.height, tabSize + 1, true, true));
        builder.append(addFloatProperty("xBuffer", this.xBuffer, tabSize + 1, true, true));
        builder.append(addFloatProperty("yBuffer", this.yBuffer, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    /**
     * Deserializes the bounds property of an object.
     * @return a new TriangleBounds object with the deserialized properties
     */
    public static TriangleBounds deserialize() {
        float base = Parser.consumeFloatProperty("Base");
        Parser.consume(',');
        float height = Parser.consumeFloatProperty("Height");
        Parser.consume(',');
        float xBuffer = Parser.consumeFloatProperty("xBuffer");
        Parser.consume(',');
        float yBuffer = Parser.consumeFloatProperty("yBuffer");
        Parser.consumeEndObjectProperty();


        TriangleBounds bounds = new TriangleBounds(base, height);
        bounds.xBuffer = xBuffer;
        bounds.yBuffer = yBuffer;
        return bounds;
    }

    /**
     * Creates a new object with triangle bounds component instead of passing the reference around.
     * @return new object = copy of triangle bounds
     */
    @Override
    public Component copy() {
        return new TriangleBounds(this.base, this.height);
    }
}
