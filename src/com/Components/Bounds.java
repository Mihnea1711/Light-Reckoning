package com.Components;

import com.Game.Component;
import com.Game.GameObject;

enum BoundsType {
    Box,
    Triangle
}

public abstract class Bounds extends Component {
    public BoundsType type;

    //abstract because we have different scenarios (Box, Triangle)
    abstract public float getWidth();
    abstract public float getHeight();

    public static boolean checkCollision(Bounds b1, Bounds b2) {
        if(b1.type == b2.type && b1.type == BoundsType.Box) {
            return BoxBounds.checkCollision((BoxBounds)b1, (BoxBounds)b2);
        } else if(b1.type == BoundsType.Box && b2.type == BoundsType.Triangle) {
            return TriangleBounds.checkCollision((BoxBounds)b1, (TriangleBounds)b2);
        } else if (b1.type == BoundsType.Triangle && b2.type == BoundsType.Box) {
            return TriangleBounds.checkCollision((TriangleBounds)b1, (BoxBounds)b2);
        }
        return false;
    }

    public static void resolveCollision(Bounds b, GameObject player) {
        if(b.type == BoundsType.Box) {
            BoxBounds box = (BoxBounds)b;
            box.resolveCollision(player);
        } else if (b.type == BoundsType.Triangle) {
            player.getComp(Player.class).die();
        }
    }
}