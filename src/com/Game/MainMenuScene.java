package com.Game;

import com.Components.*;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.Utilities.Constants;
import com.Utilities.Pair;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.RescaleOp;

/**
 * This is the Main Menu
 */
public class MainMenuScene extends Scene {
    private GameObject player;
    private GameObject mouseCursor;

    public Sprite PlayButton;
    public Sprite EditorButton;

    /**
     * Constructor
     * @param name name of menu
     */
    public MainMenuScene(String name) {
        super.Scene(name);
        this.PlayButton = AssetPool.getSprite("Assets/MainMenu/play.png");
        this.EditorButton = AssetPool.getSprite("Assets/MainMenu/editor.png");
    }

    /**
     * Initialization method
     */
    public void init() {
        initAssetPool();
        mouseCursor = new GameObject("Mouse Cursor", new Transform(new Pair()), 10);
        player = new GameObject("Player", new Transform(new Pair(Constants.MenuPlayerX, Constants.MenuPlayerY)), 0);
        SpriteSheet layer1 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerOne.png");
        SpriteSheet layer2 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerTwo.png");
        SpriteSheet layer3 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerThree.png");
        Player playerComps = new Player(layer1.sprites.get(20), layer2.sprites.get(20), layer3.sprites.get(20), Color.RED, Color.GREEN);
        player.addComponent(playerComps);

        addGameObject(player);

        initBackGrounds();
    }

    public void initAssetPool() {
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerOne.png", Constants.PlayerWidth, Constants.PlayerHeight, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerTwo.png", Constants.PlayerWidth, Constants.PlayerHeight, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerThree.png", Constants.PlayerWidth, Constants.PlayerHeight, 2, 13, 13*5);

        AssetPool.addSpritesheet("Assets/MainMenu/play.png", 121, 121, 0, 1, 1);
        AssetPool.addSpritesheet("Assets/MainMenu/editor.png", 126, 115, 0, 1, 1);
    }

    /**
     * Initialization method for backgrounds
     */
    public void initBackGrounds() {
        GameObject ground = new GameObject("Ground", new Transform(new Pair(0, Constants.MenuGround_Y)), 1);
        ground.addComponent(new Ground());
        ground.setNonserializable();
        addGameObject(ground);


        int numBackGrounds = 5;
        GameObject[] backgrounds = new GameObject[numBackGrounds];
        GameObject[] groundBgs = new GameObject[numBackGrounds];

        for(int i = 0; i < numBackGrounds; i++) {
            ParallaxBG bg = new ParallaxBG("Assets/BackGround/bg01.png", null, ground.getComp(Ground.class), false);
            int x = i * bg.sprite.width;
            int y = 0;

            GameObject obj = new GameObject("BackGround", new Transform(new Pair(x, y)), -10);
            obj.setUI(true);
            obj.addComponent(bg);
            obj.setNonserializable();
            backgrounds[i] = obj;

            ParallaxBG groundBg = new ParallaxBG("Assets/Ground/ground04.png", null, ground.getComp(Ground.class), true);
            x = i * groundBg.sprite.width;
            y = (int) ground.transform.pos.y;

            GameObject groundObj = new GameObject("GroundBG", new Transform((new Pair(x, y))), -9);
            groundObj.addComponent(groundBg);
            groundObj.setUI(true);
            groundObj.setNonserializable();
            groundBgs[i] = groundObj;

            addGameObject(obj);
            addGameObject(groundObj);
        }
    }

    /**
     * Updates objects on the screen
     * @param dTime keeps track of frames
     */
    @Override
    public void update(double dTime) {
        mouseCursor.update(dTime);
    }

    /**
     * Draws objects on the screen
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Constants.MenuBg_Color);
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        RescaleOp rescaleOp = new RescaleOp(1.3f, 0 , null);
        this.PlayButton.img = rescaleOp.filter(this.PlayButton.img, null);
        g2.drawImage(this.PlayButton.img, 460, 489, this.PlayButton.width, this.PlayButton.height, null);
        g2.drawImage(this.EditorButton.img, 640, 489, this.EditorButton.width, this.EditorButton.height, null);

        renderer.render(g2);
        mouseCursor.draw(g2);
    }

    /**
     * Don't need to implement it
     * @param filename the file from where we take our serialized level.
     */
    @Override
    protected void importLvl(String filename) {
    }
}
