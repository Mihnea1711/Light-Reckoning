package com.Game;

import com.Buttons.SceneChangerButton;
import com.Components.Ground;
import com.Components.ParallaxBG;
import com.Components.Sprite;
import com.Components.SpriteSheet;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.Utilities.Constants;
import com.Utilities.Pair;

import java.awt.Color;
import java.awt.Graphics2D;



/**
 * The scene where we select whether we want to create a new level, or to edit an existing one...
 */
public class OptionSelectMenu extends Scene{
    private GameObject mouseCursor;

    private Sprite backButton, playCreated;
    private SpriteSheet newSprites;

    /**
     * Constructor.
     * @param name scene name
     */
    public OptionSelectMenu(String name) {
        super.Scene(name);
    }

    /**
     * Initialization method.
     */
    public void init() {
        initAssetPool();
        mouseCursor = new GameObject("Mouse Cursor", new Transform(new Pair()), 10);
        initBackGrounds();
        initButtons();
    }

    /**
     * Initialization method for the asset pool.
     */
    public void initAssetPool() {
        AssetPool.addSpritesheet("Assets/UI/newSprites.png", 80, 81, 2,2, 2);
        AssetPool.addSpritesheet("Assets/UI/PlayCreated.png", 196, 81, 0,1, 1);
        AssetPool.addSpritesheet("Assets/Global/back.png", 70, 74, 0,1, 1);

        this.backButton = AssetPool.getSprite("Assets/Global/back.png");
        this.playCreated = AssetPool.getSprite("Assets/UI/PlayCreated.png");
        this.newSprites = AssetPool.getSpritesheet("Assets/UI/newSprites.png");
    }

    /**
     * Initialization method for the buttons.
     */
    public void initButtons() {
        GameObject BackButton = new GameObject("Back", new Transform(new Pair(1150, 50)), 10);
        SceneChangerButton back = new SceneChangerButton(70, 74, backButton, backButton, 2);
        BackButton.addComponent(back);
        BackButton.setUI(true);
        BackButton.setNonserializable();
        addGameObject(BackButton);

        GameObject newButton = new GameObject("New", new Transform(new Pair(450, 235)), 10);
        SceneChangerButton newB = new SceneChangerButton(80, 81, newSprites.sprites.get(0), newSprites.sprites.get(1), 7, false);
        newButton.addComponent(newB);
        newButton.setUI(true);
        newButton.setNonserializable();
        addGameObject(newButton);

        GameObject playCreatedButton = new GameObject("PlayCreated", new Transform(new Pair(644, 235)), 10);
        SceneChangerButton play = new SceneChangerButton(196, 81, playCreated, playCreated, 7, true);
        playCreatedButton.addComponent(play);
        playCreatedButton.setUI(true);
        playCreatedButton.setNonserializable();
        addGameObject(playCreatedButton);
    }

    /**
     * Initialization method for the backgrounds.
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
     * Updates the scene.
     * @param dTime keeps track of frames
     */
    @Override
    public void update(double dTime) {
        for (GameObject obj : gameObjectList) {
            obj.update(dTime);
        }

        mouseCursor.update(dTime);
    }

    /**
     * Draws on the screen.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(50f / 255.0f, 10f / 255.0f, 243f / 255.0f, 1.0f));
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        //for ground
        g2.setColor(new Color(118f / 255.0f, 10f / 255.0f, 243f / 255.0f, 1.0f));
        g2.fillRect(0, Constants.MenuGround_Y, Constants.ScreenWidth, Constants.ScreenHeight - Constants.MenuGround_Y);

        g2.setColor(new Color(174f / 255.0f, 38f / 255.0f, 176f / 255.0f, 0.7f));
        g2.fillRoundRect(395,200,500,150, 35, 35);

        renderer.render(g2);
        mouseCursor.draw(g2);
    }

    /**
     * Imports the level. No reason to implement.
     *
     * @param filename the file from where we take our serialized level.
     */
    @Override
    protected void importLvl(String filename, String zipFilePath) {

    }
}
