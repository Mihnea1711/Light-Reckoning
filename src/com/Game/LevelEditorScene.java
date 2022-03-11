package com.Game;

import com.Components.*;
import com.DataStructures.Transform;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

import java.awt.Graphics2D;
import java.awt.Color;

public class LevelEditorScene extends Scene{

    public GameObject player;
    GameObject ground;
    Grid grid;
    CameraControls cameraControls;
    GameObject mouseCursor;

    public LevelEditorScene(String name){
        super.Scene(name);  //calls the superclass(Scene) constructor
    }

    @Override
    public void init() {
        grid = new Grid();
        cameraControls = new CameraControls();
        SpriteSheet objects = new SpriteSheet("Assets/Blocks.png", Constants.TileWidth, Constants.TileWidth, 2, 6, 12);
        Sprite mouseSprite = objects.sprites.get(0);
        mouseCursor = new GameObject("Mouse Cursor", new Transform(new TwoPair()));
        mouseCursor.addComponent(new SnapToGrid(Constants.TileWidth, Constants.TileWidth));
        mouseCursor .addComponent(mouseSprite);

        player = new GameObject("game obj", new Transform(new TwoPair(300.0f, 400.0f)));
        SpriteSheet layer1 = new SpriteSheet("Assets/PlayerSprites/layerOne.png", 42, 42, 2, 13, 13*5);
        SpriteSheet layer2 = new SpriteSheet("Assets/PlayerSprites/layerTwo.png", 42, 42, 2, 13, 13*5);
        SpriteSheet layer3 = new SpriteSheet("Assets/PlayerSprites/layerThree.png", 42, 42, 2, 13, 13*5);
        Player playerComps = new Player(layer1.sprites.get(0), layer2.sprites.get(0), layer3.sprites.get(0), Color.RED, Color.GREEN);
        player.addComponent(playerComps);

        ground = new GameObject("Ground", new Transform(new TwoPair(0, Constants.GroundY)));
        ground.addComponent(new Ground());

        addGameObject(ground);
        addGameObject(player);
    }

    //call different methods and attributes on the component
    @Override
    public void update(double dTime) {
        if(camera.getPosY() > Constants.CameraOffsetGroundY) {              //if the camera off to ground is > 150, just stop it there
            camera.pos.y = Constants.CameraOffsetGroundY;
        }
        for(GameObject g : gameObjectList) {        //update every game object
            g.update(dTime);
        }
        cameraControls.update(dTime);
        grid.update(dTime);
        mouseCursor.update(dTime);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor((Color.WHITE));
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        renderer.render(g2);
        grid.draw(g2);
        mouseCursor.draw(g2);
    }
}
