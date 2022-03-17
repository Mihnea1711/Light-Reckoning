package com.Components;

import com.File.Parser;
import com.Game.Component;
import com.Game.GameObject;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class BoxBounds extends Bounds {
    public float width, height;
    public float halfWidth, halfHeight;     //calculate them once, so we don't waste cpu calculating everytime
    public TwoPair centre = new TwoPair();                  //we will change the obj every frame

    public float enclosingRadius;

    public BoxBounds (float width, float height){
        this.width = width;
        this.height = height;
        this.halfHeight = height / 2;
        this.halfWidth = width / 2;
        this.enclosingRadius = (float)Math.sqrt((this.halfWidth * this.halfWidth) + (this.halfHeight * this.halfHeight));
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

    public void resolveCollision(GameObject player) {
        BoxBounds playerBounds = player.getComp(BoxBounds.class);
        playerBounds.calculateCentre();
        this.calculateCentre();

        float dx = this.centre.x - playerBounds.centre.x;
        float dy = this.centre.y - playerBounds.centre.y;

        float combinedHalfWidths = playerBounds.halfWidth + this.halfWidth;
        float combinedHalfHeights = playerBounds.halfHeight + this.halfHeight;

        float overlapX = combinedHalfWidths - Math.abs(dx);
        float overlapY = combinedHalfHeights - Math.abs(dy);

        if(overlapX >= overlapY) {
            if(dy > 0) {
                //collision on the top of the player
                player.transform.pos.y = gameObject.getPosY() - playerBounds.getHeight();
                player.getComp(RigidBody.class).speed.y = 0;
                player.getComp(Player.class).onGround = true;
            } else {
                //collision on the bottom of the player
                player.getComp(Player.class).die();
            }
        } else {
            //collision on the left or right
            if(dx < 0 && dy <= 0.3) {
                player.transform.pos.y = gameObject.getPosY() - playerBounds.getHeight();
                player.getComp(RigidBody.class).speed.y = 0;
                player.getComp(Player.class).onGround = true;
            } else {
                player.getComp(Player.class).die();
            }
        }
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

    @Override
    public boolean rayCast(TwoPair pos) {
        return pos.x > this.gameObject.getPosX() && pos.x < this.gameObject.getPosX() + this.width &&
                pos.y > this.gameObject.getPosY() && pos.y < this.gameObject.getPosY() + this.height;
    }

    @Override
    public void draw(Graphics2D g2) {
        if(isSelected) {
            g2.setColor(Color.GREEN);
            g2.setStroke(Constants.ThickLine);
            g2.draw(new Rectangle2D.Float(
                    this.gameObject.getPosX(),
                    this.gameObject.getPosY(),
                    this.width,
                    this.height));
            g2.setStroke(Constants.Line);
        }
    }
}
