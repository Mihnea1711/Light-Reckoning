package com.Game;

import com.DataStructures.Transform;
import com.Utilities.Pair;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Should take care of drawing objects relative to the camera or the zIndex.
 */
public class Renderer {
    /**
     * game objects to draw mapped to a specific ZIndex
     */
    Map<Integer, List<GameObject>> gameObjectList;
    Camera camera;

    /**
     * Constructor with parameters.
     * @param camera camera used for the objects
     */
    public Renderer(Camera camera) {
        this.camera = camera;
        this.gameObjectList = new HashMap<>();
    }

    /**
     * Submits game objects to the renderer to be drawn every frame.
     * @param gameObject the game object
     */
    public void submit(GameObject gameObject) {
        gameObjectList.computeIfAbsent(gameObject.zIndex, k -> new ArrayList<>());  //if the z index is not present, create a new list
        gameObjectList.get(gameObject.zIndex).add(gameObject);
    }

    /**
     * Draws the objects relative to (0,0).
     * If the object is UI, we want to draw it wherever it draws itself, so no need for transforms.
     * @param g2 graphics handler
     */
    public void render(Graphics2D g2) {
        int lowestZIndex = Integer.MAX_VALUE;
        int highestZIndex = Integer.MIN_VALUE;
        for(Integer i : gameObjectList.keySet()) {      //dictionaries are not ordered!
            if(i < lowestZIndex) lowestZIndex = i;
            if(i > highestZIndex) highestZIndex = i;
        }

        int currentZIndex = lowestZIndex;
        while(currentZIndex <= highestZIndex){
            if(gameObjectList.get(currentZIndex) == null) {     //if the z index is not present, continue
                currentZIndex++;
                continue;
            }
            for(GameObject g : gameObjectList.get(currentZIndex)) {
                if (g.isUI) {
                    g.draw(g2);
                } else {
                    //we only need to know where it is on the screen
                    Transform oldTransform = new Transform(g.transform.pos, new Pair(g.getScaleX(), g.getScaleY()), g.getRotation());
                    g.transform.pos = new Pair(g.getPosX() - camera.getPosX(), g.getPosY() - camera.getPosY());

                    g.draw(g2);
                    g.transform = oldTransform;     //after drawing the changes we are moving it back in its place
                }
            }
            currentZIndex++;
        }
    }
}
