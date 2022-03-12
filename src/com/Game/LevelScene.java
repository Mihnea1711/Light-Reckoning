package com.Game;

import com.Components.*;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.File.Parser;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

import java.awt.Color;
import java.awt.Graphics2D;


public class LevelScene extends Scene {
    static LevelScene currentScene;

    public GameObject player;
    public BoxBounds playerBounds;      //we are only checking playerBounds, so we don't want to be doing getComponent every frame => save CPU cycles

    public LevelScene(String name){
        super.Scene(name);  //calls the superclass(Scene) constructor
    }

    @Override
    public void init() {
        initAssetPool();

        player = new GameObject("game obj", new Transform(new TwoPair(300.0f, 400.0f)));
        SpriteSheet layer1 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerOne.png");
        SpriteSheet layer2 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerTwo.png");
        SpriteSheet layer3 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerThree.png");
        Player playerComp = new Player(layer1.sprites.get(0), layer2.sprites.get(0), layer3.sprites.get(0), Color.RED, Color.GREEN);
        player.addComponent(playerComp);
        player.addComponent(new RigidBody(new TwoPair(300f, 0f)));
        player.addComponent(new BoxBounds(Constants.PlayerWidth, Constants.PlayerHeight));
        playerBounds = new BoxBounds(Constants.TileWidth, Constants.TileHeight);            //or playerwidth, playerheight
        player.addComponent(playerBounds);

        GameObject ground;
        ground = new GameObject("Ground", new Transform(new TwoPair(0, Constants.GroundY)));
        ground.addComponent(new Ground());

        addGameObject(player);
        addGameObject(ground);

        importLvl("Test");
    }

    public void initAssetPool() {
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerOne.png", 42, 42, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerTwo.png", 42, 42, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerThree.png", 42, 42, 2, 13, 13*5);

        AssetPool.addSpritesheet("Assets/Blocks.png", 42, 42, 2, 6, 12);
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

            Bounds b = g.getComp(Bounds.class);
            if(g != player && b != null) {
                if (Bounds.checkCollision(playerBounds, b)) {
                    System.out.println("Colliding!!");
                }
            }
        }
    }

    private void importLvl(String filename) {               //could abstract it to the Scene
        Parser.openFile(filename);

        GameObject obj = Parser.parseGameObject();
        while(obj != null) {
            addGameObject(obj);
            obj = Parser.parseGameObject();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor((Color.WHITE));
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        renderer.render(g2);
    }
}
