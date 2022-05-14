package com.Game;

import com.Buttons.SceneChangerButton;
import com.Components.*;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.File.Parser;
import com.Utilities.Constants;
import com.Utilities.Pair;

import java.awt.Color;
import java.awt.Graphics2D;
import java.math.BigDecimal;
import java.util.Objects;

import static com.main.Main.conn;

/**
 * This is the playable Level.
 */
public class LevelScene extends Scene {
    public GameObject player;
    public BoxBounds playerBounds;      //we are only checking playerBounds, so we don't want to be doing getComponent every frame => save CPU cycles
    private Sprite backButton;
    private ProgressBar progressBar;

    private float startX = Float.MAX_VALUE;
    private float finishX = Float.MIN_VALUE;
    private float levelLength = 0;

    private String filename, zipPath;
    private Percentage percentage;

    /**
     * Constructor.
     * @param name level name
     */
    public LevelScene(String name){
        super.Scene(name);  //calls the superclass(Scene) constructor
        progressBar = new ProgressBar();
        //Window.getWindow().addProgressBar(progressBar);

    }

    /**
     * Initializes the level.
     */
    @Override
    public void init(String filename, String zipFilePath, String musicFile, String backgroundPath, String groundPath, boolean importLVL) {
        initAssetPool();
        this.filename = filename;
        this.zipPath = zipFilePath;

        player = new GameObject("player", new Transform(new Pair(Constants.PlayerLevelStartX, Constants.PlayerLevelStartY)), 0);
        SpriteSheet layer1 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerOne.png");
        SpriteSheet layer2 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerTwo.png");
        SpriteSheet layer3 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerThree.png");
        Player playerComp = new Player(layer1.sprites.get(20), layer2.sprites.get(20), layer3.sprites.get(20), Color.RED, Color.GREEN);
        player.addComponent(playerComp);
        player.addComponent(new RigidBody(new Pair(Constants.PlayerSpeed, 0f)));
        playerBounds = new BoxBounds(Constants.PlayerWidth - 2, Constants.PlayerHeight - 2);
        player.addComponent(playerBounds);

        renderer.submit(player);
        //TODO:: check why not working properly
//        player.setNonserializable();
//        addGameObject(player);        //should be like this

        initBackGrounds(backgroundPath, groundPath);
        initButtons();

        importLvl(filename, zipPath);

        if(levelMusic == null) {
            levelMusic = new Music(musicFile);
        }
    }

    /**
     * Initializes all the backgrounds.
     */
    public void initBackGrounds(String backgroundPath, String groundPath) {
        GameObject text = new GameObject("Percentage Text", new Transform(new Pair(620, 50)), 10);
        percentage = new Percentage();
        text.addComponent(percentage);
        text.setUI(false);
        text.setNonserializable();
        addGameObject(text);

        GameObject ground;
        ground = new GameObject("Ground", new Transform(new Pair(0, Constants.GroundY)), 1);
        ground.addComponent(new Ground());
        addGameObject(ground);

        int numBackGrounds = 7;
        //initialize the arrays, so we can have the reference to them, even if the array is not full
        GameObject[] backgrounds = new GameObject[numBackGrounds];
        GameObject[] groundBgs = new GameObject[numBackGrounds];

        for(int i = 0; i < numBackGrounds; i++) {
            ParallaxBG bg = new ParallaxBG(backgroundPath, backgrounds, ground.getComp(Ground.class), false);
            int x = i * bg.sprite.width;   //where the background is initially
            int y = 0;

            GameObject obj = new GameObject("BackGround", new Transform(new Pair(x, y)), -10);
            obj.setUI(true);
            obj.addComponent(bg);
            backgrounds[i] = obj;

            ParallaxBG groundBg = new ParallaxBG(groundPath, groundBgs, ground.getComp(Ground.class), true);
            x = i * groundBg.sprite.width;
            y = bg.sprite.height;       //moving it to the bottom of the background itself

            GameObject groundObj = new GameObject("GroundBG", new Transform((new Pair(x, y))), -9);
            groundObj.addComponent(groundBg);
            groundObj.setUI(true);
            groundBgs[i] = groundObj;

            addGameObject(obj);
            addGameObject(groundObj);
        }
    }

    /**
     * Initializes all the buttons.
     */
    public void initButtons() {
        GameObject BackButton = new GameObject("Back", new Transform(new Pair(1100, 50)), 10);
        SceneChangerButton back = new SceneChangerButton(70, 74, backButton, backButton, "", 2, "", "", null, "", "");
        BackButton.addComponent(back);
        BackButton.setUI(true);
        BackButton.setNonserializable();
        gameObjectList.add(BackButton);
        addGameObject(BackButton);
    }

    /**
     * Loads all the sprites and sprite sheets into the Level.
     */
    public void initAssetPool() {
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerOne.png", 42, 42, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerTwo.png", 42, 42, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerThree.png", 42, 42, 2, 13, 13*5);

        AssetPool.addSpritesheet("Assets/Blocks/Blocks.png", Constants.TileWidth, Constants.TileHeight, 2, 6, 12);
        AssetPool.addSpritesheet("Assets/UI/buttonSprites.png", Constants.ButtonWidth, Constants.ButtonHeight, 2, 2, 2);
        AssetPool.addSpritesheet("Assets/UI/tabs.png", Constants.TabWidth, Constants.TabHeight, 2, 6, 6);
        AssetPool.addSpritesheet("Assets/Blocks/spikes.png", Constants.TileWidth, Constants.TileHeight, 2, 6, 4);
        AssetPool.addSpritesheet("Assets/Blocks/bigBlocks.png", Constants.TileWidth * 2, Constants.TileHeight * 2, 2, 3, 3);
        AssetPool.addSpritesheet("Assets/Blocks/smallBlocks.png", Constants.TileWidth, Constants.TileHeight, 2, 6, 6);
        AssetPool.addSpritesheet("Assets/Portals/portal.png", 44, 85, 2,2, 2);
        AssetPool.addSpritesheet("Assets/Collectibles/coin.png", 75, 75, 0,1, 1);

        AssetPool.getSprite("Assets/PlayerSprites/spaceship.png");
        AssetPool.getSprite("Assets/PlayerSprites/ufo.png");

        AssetPool.addSpritesheet("Assets/Global/back.png", 70, 74, 0,1, 1);
        this.backButton = AssetPool.getSprite("Assets/Global/back.png");
    }

    /**
     * Calls different methods and attributes on the component.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if (player.getPosX() - camera.getPosX() > Constants.CameraX) {
            camera.pos.x = player.getPosX() - Constants.CameraX;            //if the player is moving left/right, then move the camera with him
        }

        //if the player is moving up/down, move the camera with him only in normal form
        if(player.getComp(Player.class).state != PlayerState.Flying) {
            camera.pos.y = player.getPosY() - Constants.CameraY;
        }

        if(camera.getPosY() > Constants.CameraOffsetGroundY) {              //if the camera off to ground is > 150, just stop it there
            camera.pos.y = Constants.CameraOffsetGroundY;
        }

        player.update(dTime);       //update the state of player

        progressBar.setCounterValue(((player.getPosX() - startX) / levelLength) * 100);
        //progressBar.counter = new BigDecimal(String.format("%.2f", (player.getPosX() - startX) / levelLength));

        if(player.getPosX() <= finishX && (Objects.equals(filename, "Level1") || Objects.equals(filename, "Level2") ||
                Objects.equals(filename, "Level3") || Objects.equals(filename, "Level4"))) {
            DataBaseHandler.updateCounter(conn, filename, (int)Math.ceil(new BigDecimal(String.format("%.2f", ((player.getPosX() - startX) / levelLength) * 100)).doubleValue()));
        }

        player.getComp(Player.class).onGround = false;      //don't know if the player is on ground or not
        for(GameObject g : gameObjectList) {        //update every game object
            g.update(dTime);
            Bounds b = g.getComp(Bounds.class);
            if(b != null) {
                if (Bounds.checkCollision(playerBounds, b)) {
                    if(b instanceof CircleBounds) {
                        g.removeComponent(Sprite.class);
                    }
                    Bounds.resolveCollision(b, player);
                }
            }
        }
        //TODO:: check why not working properly
        //Remove this if it is too frustrating
        //progressBar.update(dTime);      //update the progress bar

        if(player.getPosX() < startX) {
            percentage.setText("0 %");
        } else {
            percentage.setText((int) progressBar.getCounterValue() + " %");
        }

    }

    /**
     * Method to import a level created into the game world.
     * Could abstract it to the Scene.
     * @param filename the file we are opening
     */
    @Override
    protected void importLvl(String filename, String zipFilePath) {               //could abstract it to the Scene
        Parser.openFile(filename, zipFilePath);

        GameObject obj = Parser.parseGameObject();
        while(obj != null) {
            if(obj.getPosX() < startX) {
                startX = obj.getPosX();
            }
            if(obj.getPosX() > finishX) {
                finishX = obj.getPosX();
                levelLength = finishX - startX;
            }
            addGameObject(obj);
            obj = Parser.parseGameObject();
        }
    }

    /**
     * Draws the level.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor((Constants.BgColor));
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        renderer.render(g2);
    }
}
