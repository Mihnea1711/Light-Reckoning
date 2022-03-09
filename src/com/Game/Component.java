package com.Game;

import java.awt.Graphics2D;

//generic class = any class can override this
public abstract class Component<T> {

    //every component is created with a game object
    public GameObject gameObject;

    //method that does nothing, so you can override it or not
    public void update(double dTime){
        return; //doing nothing
    }

    public void draw(Graphics2D g2){
        return;
    }
}
