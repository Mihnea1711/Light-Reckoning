package com.Game;

import com.DataStructures.Transform;
import com.Utilities.TwoPair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    List<GameObject> gameObjectList;
    Camera camera;

    //constructor
    public Renderer(Camera camera) {
        this.camera = camera;
        this.gameObjectList = new ArrayList<>();
    }

    //submit game objects to the renderer
    public void submit(GameObject gameObject) {
        this.gameObjectList.add(gameObject);
    }

    //draw the objects relative to (0,0)
    public void render(Graphics2D g2) {
        for(GameObject g : gameObjectList) {
            //we only need to know where it is on the screen
            Transform oldTransform = new Transform(g.transform.pos);
            oldTransform.rotation = g.getRotation();
            oldTransform.scale  = new TwoPair(g.getScaleX(), g.getScaleY());
            g.transform.pos = new TwoPair(g.getPosX() - camera.getPosX(), g.getPosY() - camera.getPosY());

            g.draw(g2);
            g.transform = oldTransform;     //after drawing the changes we are moving it back in its place
        }
    }
}
