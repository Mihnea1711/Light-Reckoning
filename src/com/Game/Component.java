package com.Game;

import com.File.Serialize;

import java.awt.Graphics2D;

//generic class = any class can override this
public abstract class Component<T> extends Serialize {

    //every component is created with a game object
    public GameObject gameObject;

    //method that does nothing, so you can override it or not
    public void update(double dTime) {
        return; //doing nothing
    }           //java removes stuff like this to now waste time

    public void draw(Graphics2D g2) {
        return;
    }

    public abstract Component copy();

    public void start() {
        return;
    }
}
