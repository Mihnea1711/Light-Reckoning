package com.Game;

import com.Components.*;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

import javax.swing.*;
import java.awt.*;

public class LevelEditorScene extends Scene{

    public GameObject player;
    GameObject ground;

    public LevelEditorScene(String name){
        super.Scene(name);  //calls the superclass(Scene) constructor
    }

    @Override
    public void init() {
        player = new GameObject("game obj", new Transform(new TwoPair(700.0f, 400.0f)));
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
        if (player.getPosX() - camera.getPosX() > Constants.CameraX) {
            camera.pos.x = player.getPosX() - Constants.CameraX;            //if the player is moving left/right, then move the camera with him
        }
        if(player.getPosY() - camera.getPosY() > Constants.CameraY){        //if the player is moving up/down, move the camera with him
            camera.pos.y = player.getPosY() - Constants.CameraY;
        }
        if(camera.getPosY() > Constants.CameraOffsetGroundY) {              //if the camera off to ground is > 150, just stop it there
            camera.pos.y = Constants.CameraOffsetGroundY;
        }
        for(GameObject g : gameObjectList) {        //update every game object
            g.update(dTime);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor((Color.WHITE));
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        renderer.render(g2);
    }
}
