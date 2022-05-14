package com.Components;

import com.File.Parser;
import com.Game.Component;
import com.Game.GameObject;
import com.Utilities.Constants;
import com.Utilities.Pair;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Class for the bounds of a box(square) objects.
 */
public class BoxBounds extends Bounds {
    private float width, height;
    public float halfWidth, halfHeight;     //calculate them once, so we don't waste cpu calculating everytime
    public Pair centre = new Pair();                  //we will change the obj contents every frame

    //these vars are for the objects that don't respect the standard block size, so we can draw them correctly.
    //used in the main container as a sort of "Prefabs"
    public float xBuffer = 0.0f;
    public float yBuffer = 0.0f;

    public float enclosingRadius;

    public boolean isTrigger;

    /**
     * Constructor for box bounds.
     * @param width width of bounds
     * @param height height of bounds
     */
    public BoxBounds (float width, float height){
        init(width, height, false);
    }

    /**
     * Constructor for box bounds if we want to create a box bounds with a trigger effect.
     * @param width width of bounds
     * @param height height of bounds
     * @param isTrigger flag for the portal trigger
     */
    public BoxBounds(float width, float height, boolean isTrigger) {
        init(width, height, isTrigger);
    }

    /**
     * Initialization method for box bounds.
     * Called inside the constructors to simplify it.
     * @param width width of bounds
     * @param height height of bounds
     * @param isTrigger flag for the portal trigger
     */
    public void init(float width, float height, boolean isTrigger) {
        this.width = width;
        this.height = height;
        this.halfHeight = height / 2.0f;
        this.halfWidth = width / 2.0f;
        this.enclosingRadius = (float)Math.sqrt((this.halfWidth * this.halfWidth) + (this.halfHeight * this.halfHeight));
        this.type = BoundsType.Box;
        this.isTrigger = isTrigger;
    }

    /**
     * This function is called after the game object is created.
     */
    @Override
    public void start() {           //the boxBounds is attached to the game object
        this.calculateCentre();
    }

    /**
     * Calculates the centre of the object.
     * Variables xBuffer, yBuffer are used for the objects that don't respect the standard square properties.
     */
    public void calculateCentre() {
        this.centre.x = this.gameObject.transform.pos.x + this.halfWidth + this.xBuffer;
        this.centre.y = this.gameObject.transform.pos.y + this.halfHeight + this.yBuffer;
    }

    /**
     * Checks the collision between 2 square objects.
     * @param b1 first object's bounds
     * @param b2 second object's bounds
     * @return whether they are colliding or not
     */
    public static boolean checkCollision(BoxBounds b1, BoxBounds b2) {
        b1.calculateCentre();
        b2.calculateCentre();

        float dx = b2.centre.x - b1.centre.x;
        float dy = b2.centre.y - b1.centre.y;

        float combinedHalfWidths = b1.halfWidth + b2.halfWidth;
        float combinedHalfHeights = b1.halfHeight + b2.halfHeight;

        if(Math.abs(dy) <= combinedHalfHeights + 4) {            //if they are colliding on the y-axis
            return Math.abs(dx) <= combinedHalfWidths;          //return whether they collide on the x-axis or not
        }
        return false;       //if not colliding
    }

    /**
     * Resolves the collisions between the player and a box(square).
     * Checks collision on top and bottom of the player, then left/right.
     * Small bug, either because the checking order is messed up, either because we need Impulse Collision resolution algorithm.
     * @param player the player
     */
    public void resolveCollision(GameObject player) {
        if (isTrigger) {
            return;
        }
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
                player.transform.pos.y = gameObject.getPosY() - playerBounds.getHeight() + yBuffer;
                player.getComp(RigidBody.class).speed.y = 0;
                player.getComp(Player.class).onGround = true;
            } else {
                //collision on the bottom of the player
                System.out.println("here1");
                player.getComp(Player.class).die();
            }
        } else {
            //collision on the left or right
            if(dx < 0 && dy <= 0.3) {
                player.transform.pos.y = gameObject.getPosY() - playerBounds.getHeight() + yBuffer;
                player.getComp(RigidBody.class).speed.y = 0;
                player.getComp(Player.class).onGround = true;
            } else {
                //TODO:: COLLISION BUG
                System.out.println("here2");
                player.getComp(Player.class).die();
            }
        }
    }

    /**
     * Creates a new object with bounds component instead of passing the reference around.
     * @return new object = copy of bounds
     */
    @Override
    public Component copy() {
        BoxBounds bounds = new BoxBounds(width, height, isTrigger);
        bounds.xBuffer = xBuffer;
        bounds.yBuffer = yBuffer;
        return bounds;
    }

    /**
     * Serializes the box bounds.
     * @param tabSize number of tabs to be indented correctly
     * @return the bounds as a string
     */
    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("BoxBounds", tabSize));
        builder.append(addFloatProperty("Width", this.width, tabSize + 1, true, true));
        builder.append(addFloatProperty("Height", this.height, tabSize + 1, true, true));
        builder.append(addFloatProperty("xBuffer", this.xBuffer, tabSize + 1, true, true));
        builder.append(addFloatProperty("yBuffer", this.yBuffer, tabSize + 1, true, true));
        builder.append(addBooleanProperty("isTrigger", this.isTrigger, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    /**
     * Deserializes the bound property of an object.
     * @return a new BoxBounds object with the deserialized properties
     */
    public static BoxBounds deserialize() {
        float width = Parser.consumeFloatProperty("Width");
        Parser.consume(',');
        float height = Parser.consumeFloatProperty("Height");
        Parser.consume(',');
        float xBuffer = Parser.consumeFloatProperty("xBuffer");
        Parser.consume(',');
        float yBuffer = Parser.consumeFloatProperty("yBuffer");
        Parser.consume(',');
        boolean isTrigger = Parser.consumeBooleanProperty("isTrigger");
        Parser.consumeEndObjectProperty();

        BoxBounds bounds = new BoxBounds(width, height, isTrigger);
        bounds.xBuffer = xBuffer;
        bounds.yBuffer = yBuffer;
        return bounds;
    }

    /**
     * Helper method.
     * @return width of bounds
     */
    @Override
    public float getWidth() {
        return this.width;
    }

    /**
     * Helper method.
     * @return height of bounds
     */
    @Override
    public float getHeight() {
        return this.height;
    }

    /**
     * Checks if a point is inside a box.
     * @param pos position of point
     * @return true/false
     */
    @Override
    public boolean rayCast(Pair pos) {
        return pos.x > this.gameObject.getPosX() + xBuffer && pos.x < this.gameObject.getPosX() + this.width + xBuffer &&
                pos.y > this.gameObject.getPosY() + yBuffer && pos.y < this.gameObject.getPosY() + this.height + yBuffer;
    }

    /**
     * No need to update the bounds.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {    }

    /**
     * Draws the bounds of the box selected.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        if(isSelected) {
            g2.setColor(Color.GREEN);
            g2.setStroke(Constants.ThickLine);
            g2.draw(new Rectangle2D.Float(
                    this.gameObject.getPosX() + xBuffer,
                    this.gameObject.getPosY() + yBuffer,
                    this.width,
                    this.height));
            g2.setStroke(Constants.Line);
        }
    }
}
