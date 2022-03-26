package com.Game;

import com.Components.Ground;
import com.Components.ParallaxBG;
import com.DataStructures.Transform;
import com.Utilities.Constants;
import com.Utilities.Pair;

import java.awt.Graphics2D;

/**
 * This is the Main Menu
 */
public class MainMenuScene extends Scene {

    private GameObject mouseCursor;

    /**
     * Constructor
     * @param name name of menu
     */
    public MainMenuScene(String name) {
        super.Scene(name);
    }

    /**
     * Initialization method
     */
    public void init() {
        initAssetPool();
        mouseCursor = new GameObject("Mouse Cursor", new Transform(new Pair()), 10);
        initBackGrounds();
    }

    public void initAssetPool() {

    }

    /**
     * Initialization method for backgrounds
     */
    public void initBackGrounds() {
        GameObject ground = new GameObject("Ground", new Transform(new Pair(0, Constants.Ground_Y)), 1);
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
            groundBg.setGroundColor(Constants.Ground_Color);

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
        g2.setColor(Constants.Bg_Color);
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

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
