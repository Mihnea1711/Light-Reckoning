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
    Renderer renderer;

    public void Scene(String name){
        this.name = name;
        this.camera = new Camera(new TwoPair());
        this.gameObjectList = new ArrayList<>();
        this.renderer = new Renderer(this.camera);
    }

    public void init(){        //initializes update

    }

    public void addGameObject(GameObject gameObject) {
        gameObjectList.add(gameObject);
        renderer.submit(gameObject);
    }
    public abstract void update(double deltaTime);  //updates the scene
    public abstract void draw(Graphics2D g);        //draw onto the screen

}
