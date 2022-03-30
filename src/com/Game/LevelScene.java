package com.Game;

import com.Components.*;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.File.Parser;
import com.Utilities.Constants;
import com.Utilities.Pair;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * This is the playable Level
 */
public class LevelScene extends Scene {
    public GameObject player;
    public BoxBounds playerBounds;      //we are only checking playerBounds, so we don't want to be doing getComponent every frame => save CPU cycles

    /**
     * Constructor
     * @param name level name
     */
    public LevelScene(String name){
        super.Scene(name);  //calls the superclass(Scene) constructor
    }

    /**
     * Initializes the level
     */
    @Override
    public void init() {
        initAssetPool();

        player = new GameObject("player", new Transform(new Pair(Constants.PlayerLevelStartX, Constants.PlayerLevelStartY)), 0);
        SpriteSheet layer1 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerOne.png");
        SpriteSheet layer2 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerTwo.png");
        SpriteSheet layer3 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerThree.png");
        Player playerComp = new Player(layer1.sprites.get(20), layer2.sprites.get(20), layer3.sprites.get(20), Color.RED, Color.CYAN);
        player.addComponent(playerComp);
        player.addComponent(new RigidBody(new Pair(Constants.PlayerSpeed, 0f)));
        playerBounds = new BoxBounds(Constants.PlayerWidth - 2, Constants.PlayerHeight - 2);
        player.addComponent(playerBounds);

        renderer.submit(player);
        //TODO:: check why not working properly
        //addGameObject(player);        //should be like this

        initBackGrounds();

        importLvl("Test");
    }

    /**
     * Initializes all the backgrounds
     */
    public void initBackGrounds() {
        GameObject ground;
        ground = new GameObject("Ground", new Transform(new Pair(0, Constants.GroundY)), 1);
        ground.addComponent(new Ground());
        addGameObject(ground);

        int numBackGrounds = 7;
        //initialize the arrays, so we can have the reference to them, even if the array is not full
        GameObject[] backgrounds = new GameObject[numBackGrounds];
        GameObject[] groundBgs = new GameObject[numBackGrounds];

        for(int i = 0; i < numBackGrounds; i++) {
            ParallaxBG bg = new ParallaxBG("Assets/BackGround/bg01.png", backgrounds, ground.getComp(Ground.class), false);
            int x = i * bg.sprite.width;   //where the background is initially
            int y = 0;

            GameObject obj = new GameObject("BackGround", new Transform(new Pair(x, y)), -10);
            obj.setUI(true);
            obj.addComponent(bg);
            backgrounds[i] = obj;

            ParallaxBG groundBg = new ParallaxBG("Assets/Ground/ground01.png", groundBgs, ground.getComp(Ground.class), true);
            x = i * groundBg.sprite.width;
            y = bg.sprite.height;       //moving it to the bottom of the background itself
            groundBg.setGroundColor(Constants.GroundColor);

            GameObject groundObj = new GameObject("GroundBG", new Transform((new Pair(x, y))), -9);
            groundObj.addComponent(groundBg);
            groundObj.setUI(true);
            groundBgs[i] = groundObj;

            addGameObject(obj);
            addGameObject(groundObj);
        }
    }

    /**
     * Loads all the sprites and sprite sheets into the Level.
     */
    public void initAssetPool() {
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerOne.png", 42, 42, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerTwo.png", 42, 42, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerThree.png", 42, 42, 2, 13, 13*5);

        AssetPool.addSpritesheet("Assets/Blocks.png", 42, 42, 2, 6, 12);

        AssetPool.getSprite("Assets/PlayerSprites/spaceship.png");
        AssetPool.getSprite("Assets/PlayerSprites/ufo.png");
    }

    /**
     * Calls different methods and attributes on the component
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if (player.getPosX() - camera.getPosX() > Constants.CameraX) {
            camera.pos.x = player.getPosX() - Constants.CameraX;            //if the player is moving left/right, then move the camera with him
        }

        //if the player is moving up/down, move the camera with him
        camera.pos.y = player.getPosY() - Constants.CameraY;

        if(camera.getPosY() > Constants.CameraOffsetGroundY) {              //if the camera off to ground is > 150, just stop it there
            camera.pos.y = Constants.CameraOffsetGroundY;
        }

        player.update(dTime);       //update the state of player
        player.getComp(Player.class).onGround = false;      //don't know if the player is on ground or not

        for(GameObject g : gameObjectList) {        //update every game object
            g.update(dTime);
            Bounds b = g.getComp(Bounds.class);
            if(b != null) {
                if (Bounds.checkCollision(playerBounds, b)) {
                    Bounds.resolveCollision(b, player);
                }
            }
        }
    }

    /**
     * Method to import a level created into the game world.
     * Could abstract it to the Scene.
     * @param filename the file we are opening
     */
    @Override
    protected void importLvl(String filename) {               //could abstract it to the Scene
        Parser.openFile(filename);

        GameObject obj = Parser.parseGameObject();
        while(obj != null) {
            addGameObject(obj);
            obj = Parser.parseGameObject();
        }
    }

    /**
     * Draws the level
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor((Constants.BgColor));
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        renderer.render(g2);
    }
}
