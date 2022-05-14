package com.Game;

import com.Buttons.SceneChangerButton;
import com.Components.Ground;
import com.Components.ParallaxBG;
import com.Components.Sprite;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.Utilities.Constants;
import com.Utilities.Pair;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import static com.main.Main.conn;

/**
 * Class for the level's info.
 * Connects to the database and gets the info for the specified level.
 */
public class InfoScene extends Scene{
    private GameObject mouseCursor;
    private List<GameObject> buttons;

    private Sprite okButton;

    private int prevSceneNumber;

    /**
     * Constructor
     * @param name scene name
     */
    public InfoScene(String name) {
        super.Scene(name);
        this.buttons = new ArrayList<>();
    }

    /**
     * Initialization method.
     * @param prevSceneNumber scene index of the level's stats
     */
    public void init(int prevSceneNumber) {
        this.prevSceneNumber = prevSceneNumber;
        initAssetPool();
        mouseCursor = new GameObject("Mouse Cursor", new Transform(new Pair()), 10);
        initBackGrounds();
        initButtons();
    }

    /**
     * Initialization of the asset pool of the scene.
     */
    public void initAssetPool() {
        AssetPool.addSpritesheet("Assets/UI/ok.png", 145, 60, 0, 1, 1);
        this.okButton = AssetPool.getSprite("Assets/UI/ok.png");
    }

    /**
     * Initialization of the scene's backgrounds.
     */
    public void initBackGrounds() {
        GameObject ground = new GameObject("Ground", new Transform(new Pair(0, Constants.MenuGround_Y)), 1);
        ground.addComponent(new Ground());
        ground.setNonserializable();
        addGameObject(ground);

        int numBackGrounds = 5;
        GameObject[] groundBgs = new GameObject[numBackGrounds];

        for(int i = 0; i < numBackGrounds; i++) {
            ParallaxBG groundBg = new ParallaxBG("Assets/Ground/ground05.png", null, ground.getComp(Ground.class), true);
            int x = i * groundBg.sprite.width;
            int y = (int) ground.transform.pos.y;

            GameObject groundObj = new GameObject("GroundBG", new Transform((new Pair(x, y))), -9);
            groundObj.addComponent(groundBg);
            groundObj.setUI(true);
            groundObj.setNonserializable();
            groundBgs[i] = groundObj;

            addGameObject(groundObj);
        }
    }

    /**
     * Initialization of the scene's buttons.
     */
    public void initButtons() {
        GameObject ok = new GameObject("OKButton", new Transform(new Pair(568, 500)), 10);
        SceneChangerButton OK = new SceneChangerButton(okButton.width, okButton.height, okButton, okButton, prevSceneNumber);
        ok.addComponent(OK);
        ok.setUI(true);
        ok.setNonserializable();
        buttons.add(ok);

        addGameObject(ok);
    }

    /**
     * Updates the scene
     * @param dTime keeps track of frames
     */
    @Override
    public void update(double dTime) {
        for (GameObject obj : buttons) {
            obj.update(dTime);
        }
        mouseCursor.update(dTime);
    }

    /**
     * Utility method to draw the level stats on the screen.
     * @param g2 graphics handler
     */
    private void drawStats(Graphics2D g2) {
        g2.setColor(new Color(200f / 255.0f, 80f / 255.0f, 176f / 255.0f, 0.6f));
        g2.fillRoundRect(395,100,500,350, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Calibri", Font.BOLD, 40));
        g2.drawString("LEVEL STATS", 530, 160);

        g2.setFont(new Font("Calibri", Font.PLAIN, 25));
        g2.drawString("Total Attempts:  " + DataBaseHandler.getAttempts(conn, name), 420, 230);
        g2.drawString("Total Jumps:  " + DataBaseHandler.getJumps(conn, name), 420, 280);
        g2.drawString("Completion:  " + DataBaseHandler.getCounter(conn, name) + "%", 420, 330);
        g2.drawString("Coins Collected:  " + DataBaseHandler.getCoins(conn, name), 420, 380);
    }

    /**
     * Utility method to draw the main menu stats on the screen.
     * @param g2 graphics handler
     */
    private void drawMainMenuStats(Graphics2D g2) {
        g2.setColor(new Color(200f / 255.0f, 80f / 255.0f, 176f / 255.0f, 0.6f));
        g2.fillRoundRect(395,100,500,350, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Calibri", Font.BOLD, 40));
        g2.drawString("GAME STATS", 530, 160);

        g2.setFont(new Font("Calibri", Font.PLAIN, 25));
        g2.drawString("Total Attempts:  " + DataBaseHandler.getAllAttempts(conn), 420, 230);
        g2.drawString("Total Jumps:  " + DataBaseHandler.getAllJumps(conn), 420, 280);
        g2.drawString("Completed Levels:  " + DataBaseHandler.getAllCompletedLevels(conn), 420, 330);
        g2.drawString("Coins Collected:  " + DataBaseHandler.getAllCoins(conn), 420, 380);
    }

    /**
     * Draws on the screen
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        g2.setColor(new Color(118f / 255.0f, 10f / 255.0f, 243f / 255.0f, 1.0f));
        g2.fillRect(0, Constants.MenuGround_Y, Constants.ScreenWidth, Constants.ScreenHeight - Constants.MenuGround_Y);

        renderer.render(g2);
        if(prevSceneNumber != 2) {
            drawStats(g2);
        } else {
            drawMainMenuStats(g2);
        }
        mouseCursor.draw(g2);
    }

    /**
     * Imports the level. No reason to implement.
     * @param filename the file from where we take our serialized level.
     */
    @Override
    protected void importLvl(String filename, String zipFilePath) {

    }
}
