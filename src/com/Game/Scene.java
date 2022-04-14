package com.Game;

import com.Components.Music;
import com.Utilities.Pair;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Abstract class that
 * holds the methods that all the scenes will need
 */
public abstract class Scene {
    public String name;        //helpful for debugging
    public Camera camera;   //camera for the scene
    protected List<GameObject> gameObjectList;    //all the game objects in the scene

    //prepares the objects that are going to be removed, because it will be called in the middle of an update loop.
    //if we delete objects and then we try to update them, it will cause problems, so we will wait till the end of the frame.
    protected List<GameObject> objsToRemove;

    protected Renderer renderer;      //renderer for the scene

    protected Music levelMusic = null;

    protected static ArrayList<String> levelsCreated = new ArrayList<>();

    /**
     * Constructor for the Scene
     * @param name name of the scene
     */
    public void Scene(String name){
        this.name = name;
        this.camera = new Camera(new Pair());
        this.gameObjectList = new ArrayList<>();
        this.objsToRemove = new ArrayList<>();
        updateCreatedLevels();
        this.renderer = new Renderer(this.camera);
    }

    /**
     * Initializes update
     */
    public void init(){

    }

    public  void init(String filename, String zipFilePath, String musicFile, String backgroundPath, String groundPath, boolean importLvl) {

    }

    private void updateCreatedLevels() {
        ZipFile levels;
        try {
            File check = new File("levels/CreatedLevels.zip");
            if(check.exists()) {
                levels = new ZipFile("levels/CreatedLevels.zip");

                //copy contents from existing zip
                Enumeration<? extends ZipEntry> entries = levels.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = entries.nextElement();
                    if (!levelsCreated.contains(zipEntry.getName().substring(0, zipEntry.getName().length() - 5))) {
                        levelsCreated.add(zipEntry.getName().substring(0, zipEntry.getName().length() - 5));
                    }
                }
                levels.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
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

    public void addCreatedLevel(String name) {
        levelsCreated.add(name);
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
    protected abstract void importLvl(String filename, String zipFilePath);
}
