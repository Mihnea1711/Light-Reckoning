package com.Game;

import com.DataStructures.Transform;
import com.Utilities.TwoPair;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Renderer {
    Map<Integer, List<GameObject>> gameObjectList;
    Camera camera;

    //constructor
    public Renderer(Camera camera) {
        this.camera = camera;
        this.gameObjectList = new HashMap<>();
    }

    //submit game objects to the renderer
    public void submit(GameObject gameObject) {
        gameObjectList.computeIfAbsent(gameObject.zIndex, k -> new ArrayList<>());
        gameObjectList.get(gameObject.zIndex).add(gameObject);
    }

    //draw the objects relative to (0,0)
    public void render(Graphics2D g2) {
        int lowestZIndex = Integer.MAX_VALUE;
        int highestZIndex = Integer.MIN_VALUE;
        for(Integer i : gameObjectList.keySet()) {
            if(i < lowestZIndex) lowestZIndex = i;
            if(i > highestZIndex) highestZIndex = i;
        }

        int currentZIndex = lowestZIndex;
        while(currentZIndex <= highestZIndex){
            if(gameObjectList.get(currentZIndex) == null) {
                currentZIndex++;
                continue;
            }
            for(GameObject g : gameObjectList.get(currentZIndex)) {
                if (g.isUI) {
                    g.draw(g2);
                } else {
                    //we only need to know where it is on the screen
                    Transform oldTransform = new Transform(g.transform.pos);
                    oldTransform.rotation = g.getRotation();
                    oldTransform.scale = new TwoPair(g.getScaleX(), g.getScaleY());
                    g.transform.pos = new TwoPair(g.getPosX() - camera.getPosX(), g.getPosY() - camera.getPosY());

                    g.draw(g2);
                    g.transform = oldTransform;     //after drawing the changes we are moving it back in its place
                }
            }
            currentZIndex++;
        }
    }
}
