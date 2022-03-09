package com.Game;

import java.awt.*;

//hold the methods that all diff scenes will need
public abstract class Scene {
    String name;

    public void Scene(String name){
        this.name = name;
        init();
    }

    public void init(){        //initialize update

    }
    public abstract void update(double deltaTime);  //updates the scene
    public abstract void draw(Graphics2D g);        //draw onto the screen

}
