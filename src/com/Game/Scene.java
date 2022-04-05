package com.Game;

import com.Components.Music;
import com.Utilities.Pair;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that
 * holds the methods that all the scenes will need
 */
public abstract class Scene {
    String name;        //helpful for debugging
    public Camera camera;   //camera for the scene
    List<GameObject> gameObjectList;    //all the game objects in the scene
    //prepares the objects that are going to be removed, because it will be called in the middle of an update loop.
    //if we delete objects and then we try to update them, it will cause problems, so we will wait till the end of the frame.
    List<GameObject> objsToRemove;
    Renderer renderer;      //renderer for the scene

    protected Music levelMusic = null;

    //protected Music SceneSoundTrack = null;

    /**
     * Constructor for the Scene
     * @param name name of the scene
     */
    public void Scene(String name){
        this.name = name;
        this.camera = new Camera(new Pair());
        this.gameObjectList = new ArrayList<>();
        this.objsToRemove = new ArrayList<>();
        this.renderer = new Renderer(this.camera);
    }

    /**
     * Initializes update
     */
    public void init(){

    }

    public  void init(String filename, String musicFile) {

    }

    /**
     * Helper method
     * @return list of all the game objects inside a scene
     */
    public List<GameObject> getAllGameObjects() {
        return gameObjectList;
    }

    /**
     * Removes an object from the scene, putting it inside a list of object to be removed
     * @param obj object to be removed
     */
    public void removeGameObject(GameObject obj) {
        objsToRemove.add(obj);
    }

    /**
     * Method to add the game object to the renderer and to the list
     * @param gameObject the object
     */
    public void addGameObject(GameObject gameObject) {
        gameObjectList.add(gameObject);
        renderer.submit(gameObject);
        for(Component c : gameObject.getAllComponents()) {
            c.start();
        }
    }

    /**
     * Updates the scene
     * @param dTime keeps track of frames
     */
    public abstract void update(double dTime);

    /**
     * Draws on the screen
     * @param g2 graphics handler
     */
    public abstract void draw(Graphics2D g2);

    /**
     * Imports the level
     * @param filename the file from where we take our serialized level.
     */
    protected abstract void importLvl(String filename);
}
