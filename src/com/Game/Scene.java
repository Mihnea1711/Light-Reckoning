package com.Game;

import com.Utilities.TwoPair;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

//hold the methods that all diff scenes will need
public abstract class Scene {
    String name;
    public Camera camera;
    List<GameObject> gameObjectList;
    List<GameObject> objsToRemove;
    Renderer renderer;

    public void Scene(String name){
        this.name = name;
        this.camera = new Camera(new TwoPair());
        this.gameObjectList = new ArrayList<>();
        this.objsToRemove = new ArrayList<>();
        this.renderer = new Renderer(this.camera);
    }

    public void init(){        //initializes update

    }

    public List<GameObject> getAllGameObjects() {
        return gameObjectList;
    }

    public void removeGameObject(GameObject obj) {
        objsToRemove.add(obj);
    }

    public void addGameObject(GameObject gameObject) {
        gameObjectList.add(gameObject);
        renderer.submit(gameObject);
        for(Component c : gameObject.getAllComponents()) {
            c.start();
        }
    }
    public abstract void update(double deltaTime);  //updates the scene
    public abstract void draw(Graphics2D g);        //draw onto the screen

}
