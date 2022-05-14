package com.Game;

import com.File.Serialize;

import java.awt.Graphics2D;

/**
 * Generic abstract class = any class can override this.
 * @param <T> type of component
 */
public abstract class Component<T> extends Serialize {
    /**
     * the game object attached to the component
     */
    public GameObject gameObject;

    /**
     * Empty update method that does nothing, so you can override it or not.
     * @param dTime frames
     */
    public void update(double dTime) {
        return; //java removes stuff like this to not waste time
    }

    /**
     * Empty draw method that does nothing, so you can override it or not.
     * @param g2 graphics handler
     */
    public void draw(Graphics2D g2) {
        return;
    }

    /**
     * Abstract method to force every derived class to implement it.
     * @return a new object = copy of a Component
     */
    public abstract Component copy();

    /**
     * The start method for each component.
     */
    public void start() {
        return;
    }
}
