package com.Components;

import com.Game.Component;
import com.Game.GameObject;
import com.Utilities.Pair;

/**
 * Just to differentiate between the box and triangle bounds.
 */
enum BoundsType {
    Box,
    Triangle,
    Circle
}

/**
 * Class for the bounds of the objects.
 */
public abstract class Bounds extends Component {
    /**
     * Object type.
     */
    public BoundsType type;
    /**
     * Flag whether the object is selected or not.
     */
    public boolean isSelected;

    /**
     * Abstract because we have different scenarios (Box, Triangle).
     * @return the width
     */
    abstract public float getWidth();

    /**
     * Abstract because we have different scenarios (Box, Triangle).
     * @return the height
     */
    abstract public float getHeight();

    /**
     * Function to check if a point is within some bounds.
     * @param pos position of point
     * @return true/false
     */
    abstract public boolean rayCast(Pair pos);

    /**
     * Checks the collision between 2 box objects.
     * @param b1 first object bounds
     * @param b2 second object bounds
     * @return true/false
     */
    public static boolean checkCollision(Bounds b1, Bounds b2) {
        //we know that at least 1 is the player (box)
        if (b1.type == b2.type && b1.type == BoundsType.Box) {
            return BoxBounds.checkCollision((BoxBounds) b1, (BoxBounds) b2);
        } else if (b1.type == BoundsType.Box && b2.type == BoundsType.Triangle) {
            return TriangleBounds.checkCollision((BoxBounds) b1, (TriangleBounds) b2);
        } else if (b1.type == BoundsType.Triangle && b2.type == BoundsType.Box) {
            return TriangleBounds.checkCollision((BoxBounds) b2, (TriangleBounds) b1);
        } else if (b1.type == BoundsType.Box && b2.type == BoundsType.Circle) {
            return CircleBounds.checkCollision((BoxBounds) b1, (CircleBounds) b2);
        } else if (b1.type == BoundsType.Circle && b2.type == BoundsType.Box) {
            return CircleBounds.checkCollision((BoxBounds) b2, (CircleBounds) b1);
        }
        return false;
    }

    /**
     * Resolves the collision between the player and the object he is colliding with.
     * @param b the object's bounds we are colliding with
     * @param player the player's bounds
     */
    public static void resolveCollision(Bounds b, GameObject player) {
        if(b.type == BoundsType.Box) {
            BoxBounds box = (BoxBounds)b;
            box.resolveCollision(player);
        } else if (b.type == BoundsType.Triangle) {
            player.getComp(Player.class).die();
        } else if(b.type == BoundsType.Circle) {
            CircleBounds circle = (CircleBounds)b;
            circle.resolveCollision(player);
        }
    }
}