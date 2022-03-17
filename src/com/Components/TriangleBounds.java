package com.Components;

import com.File.Parser;
import com.Game.Component;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

import java.awt.Graphics2D;

public class TriangleBounds extends Bounds{
    private float base, height, halfWidth, halfHeight;
    private float enclosingRadius;

    private float x1, x2, x3, y1, y2, y3;

    //binary
    private final int Inside = 0;
    private final int Left = 1;
    private final int Right = 2;
    private final int Bottom = 4;
    private final int Top = 8;


    public TriangleBounds(float base, float height) {
        this.type = BoundsType.Triangle;
        this.base = base;
        this.height = height;
        this.halfHeight = this.height / 2.0f;
        this.halfWidth = this.base / 2.0f;
        this.enclosingRadius = Math.max(this.halfHeight, this.halfWidth);
    }

    @Override
    public void start() {
        calculateTransform();
    }

    public static boolean checkCollision(BoxBounds b, TriangleBounds t) {
        if(t.broadPhase(b)) {
            return t.narrowPhase(b);
        }
        return false;
    }

    private boolean broadPhase(BoxBounds b) {
        //circle collision
        float bRadius = b.enclosingRadius;
        float tRadius = this.enclosingRadius;

        float centreX = this.x2;
        float centreY = this.y2 + halfHeight;

        float playerCentreX = b.gameObject.getPosX() + b.halfWidth;
        float playerCentreY = b.gameObject.getPosY() + b.halfHeight;

        TwoPair dist = new TwoPair(playerCentreX - centreX, playerCentreY - centreY);
        float magSquared = (dist.x * dist.x) + (dist.y * dist.y);
        float radSquared = (bRadius + tRadius) * (bRadius + tRadius);

        return magSquared <= radSquared;
    }

    private boolean narrowPhase(BoxBounds b) {
        TwoPair p1 = new TwoPair(x1, y1);
        TwoPair p2 = new TwoPair(x2, y2);
        TwoPair p3 = new TwoPair(x3, y3);

        TwoPair origin = new TwoPair(b.gameObject.getPosX() + b.halfWidth, b.gameObject.getPosY() + b.halfHeight);

        float rAngle = (float)Math.toRadians(b.gameObject.getRotation());

        p1 = rotatePoint(rAngle, p1, origin);
        p2 = rotatePoint(rAngle, p2, origin);
        p3 = rotatePoint(rAngle, p3, origin);

        return (boxIntersectingLine(p1, p2, 0, b, b.gameObject.transform.pos) ||
                boxIntersectingLine(p1, p3, 0, b, b.gameObject.transform.pos) ||
                boxIntersectingLine(p2, p3, 0, b, b.gameObject.transform.pos));
    }

    private boolean boxIntersectingLine(TwoPair p1, TwoPair p2, int depth, BoxBounds bounds, TwoPair pos) {
        //Cohen Sutherland clipping algorithm
        if(depth > 5) {
            System.out.println("Max depth exceeded");
            return true;
        }
        int code1 = computeRegionCode(p1, bounds);
        int code2 = computeRegionCode(p2, bounds);

        //check if line is completely inside/outside/half in/half out
        if(code1 == code2 && code1 == 0) {
            //line inside
            return true;
        } else if ((code1 & code2) != 0) {
            //line completely out
            return false;
        } else if(code1 == 0 || code2 == 0) {
            //intersecting (1 point inside)
            return true;
        }

        int xmax = (int)(pos.x + bounds.width);
        int xmin = (int)pos.x;

        //y = mx + b
        float m = (p2.y - p1.y) / (p2.x - p1.x);
        float b = p2.y - (m * p2.x);

        if((code1 & Left) == Left) {
            //add 1 to ensure we re inside clipping polygon
            p1.x = xmin + 1;
        } else if((code1 & Right) == Right) {
            //substract 1
            p1.x = xmax - 1;
        }
        p1.y = (m * p1.x) + b;

        //now repeat fo the p2
        if((code2 & Left) == Left) {
            //add 1 to ensure we re inside clipping polygon
            p2.x = xmin + 1;
        } else if((code2 & Right) == Right) {
            //substract 1
            p2.x = xmax - 1;
        }
        p2.y = (m * p2.x) + b;

        return boxIntersectingLine(p1, p2, depth + 1, bounds, pos);
    }

    private int computeRegionCode(TwoPair point, BoxBounds bounds) {
        int code = Inside;
        TwoPair topLeft = bounds.gameObject.transform.pos;

        //check if the point is to the left or to the right of bounds
        if(point.x < topLeft.x) {
            code |= Left;
        } else if (point.x > topLeft.x + bounds.width) {
            code |= Right;
        }

        //check if the point is below or above
        if(point.y < topLeft.y) {
            code |= Top;
        } else if(point.y > topLeft.y + bounds.height) {
            code |= Bottom;
        }

        return code;
    }

    //rotate the point
    private TwoPair rotatePoint(double angle, TwoPair p, TwoPair o) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        TwoPair newTwoPair = new TwoPair(p.x, p.y);

        newTwoPair.x -= o.x;
        newTwoPair.y -= o.y;

        float newX = (float)((newTwoPair.x * cos) - (newTwoPair.y * sin));
        float newY = (float)((newTwoPair.x * sin) + (newTwoPair.y * cos));

        return new TwoPair(newX + o.x, newY + o.y);
    }

    private void calculateTransform() {
        double rAngle = Math.toRadians(gameObject.getRotation());
        TwoPair p1 = new TwoPair(gameObject.getPosX(), gameObject.getPosY() + height);
        TwoPair p2 = new TwoPair(gameObject.getPosX() + halfWidth, gameObject.getPosY());
        TwoPair p3 = new TwoPair(gameObject.getPosX() + base, gameObject.getPosY() + height);
        TwoPair origin = new TwoPair(gameObject.getPosX() + (Constants.TileWidth / 2.0f),
                gameObject.getPosY() + (Constants.TileHeight / 2.0f));

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

    @Override
    public float getWidth() {
        return this.base;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public void draw(Graphics2D g2) {
//        g2.setColor(Color.GREEN);
//        g2.draw(new Line2D.Float(this.x1, this.y1, this.x2, this.y2));
//        g2.draw(new Line2D.Float(this.x1, this.y1, this.x3, this.y3));
//        g2.draw(new Line2D.Float(this.x3, this.y3, this.x2, this.y2));
    }

    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("TriangleBounds", tabSize));
        builder.append(addFloatProperty("Base", this.base, tabSize + 1, true, true));
        builder.append(addFloatProperty("Height", this.height, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    public static TriangleBounds deserialize() {
        float base = Parser.consumeFloatProperty("Base");
        Parser.consume(',');
        float height = Parser.consumeFloatProperty("Height");
        Parser.consumeEndObjectProperty();

        return new TriangleBounds(base, height);
    }

    @Override
    public Component copy() {
        return new TriangleBounds(this.base, this.height);
    }
}
