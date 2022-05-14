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
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Fourth Level Scene.
 */
public class Level4Menu extends Scene{
    private GameObject mouseCursor;

    /**
     * list of the buttons inside the scene
     */
    private List<GameObject> buttons;

    private Sprite logo3;
    private Sprite levelButtonSprite;
    private Sprite LeftArrowKey, RightArrowKey;
    private Sprite backButton;
    private Sprite infoButton;

    /**
     * Constructor.
     * @param name name of the scene
     */
    public Level4Menu(String name) {
        super.Scene(name);
        this.buttons = new ArrayList<>();
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
        AssetPool.addSpritesheet("Assets/MainMenu/Logos/LogoLevel3.png", Constants.LogoSize, Constants.LogoSize, 0, 1, 1);
        AssetPool.addSpritesheet("Assets/Global/LevelButtonSprite.png", 600, 84, 0, 1, 1);
        AssetPool.addSpritesheet("Assets/UI/info.png", 60, 61, 0, 1, 1);

        this.infoButton = AssetPool.getSprite("Assets/UI/info.png");
        this.logo3 = AssetPool.getSprite("Assets/MainMenu/Logos/LogoLevel3.png");
        this.levelButtonSprite = AssetPool.getSprite("Assets/Global/LevelButtonSprite.png");

        this.RightArrowKey = AssetPool.getSprite("Assets/Level1Menu/RightArrow.png");
        this.LeftArrowKey = AssetPool.getSprite("Assets/Level1Menu/LeftArrow.png");

        AssetPool.addSpritesheet("Assets/Global/back.png", 70, 74, 0,1, 1);
        this.backButton = AssetPool.getSprite("Assets/Global/back.png");
    }

    /**
     * Initialization method for the scene's backgrounds.
     */
    public void initBackGrounds() {
        GameObject ground = new GameObject("Ground", new Transform(new Pair(0, Constants.MenuGround_Y)), 1);
        ground.addComponent(new Ground());
        ground.setNonserializable();
        addGameObject(ground);

        GameObject noob = new GameObject("Normal", new Transform(new Pair(365, 172)), 6);
        noob.addComponent(new Sprite(logo3.img, "Assets/MainMenu/Logos/LogoLevel1.png"));
        noob.setNonserializable();
        addGameObject(noob);

        int numBackGrounds = 5;
        GameObject[] groundBgs = new GameObject[numBackGrounds];

        for(int i = 0; i < numBackGrounds; i++) {
            ParallaxBG groundBg = new ParallaxBG("Assets/Ground/ground03.png", null, ground.getComp(Ground.class), true);
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
     * Initialization method for the scene's buttons.
     */
    public void initButtons() {
        GameObject levelButton = new GameObject("SceneChangerButton", new Transform(new Pair(340, 150)), 5);
        SceneChangerButton playButton = new SceneChangerButton(levelButtonSprite.width, levelButtonSprite.height, levelButtonSprite, levelButtonSprite,
                "Fingerdash", 1, "Level4", "levels/levels.zip","Assets/LevelSoundTracks/Fingerdash.wav",
                "Assets/Background/bg05.png", "Assets/Ground/ground05.png");
        levelButton.addComponent(playButton);
        levelButton.setUI(true);
        levelButton.setNonserializable();
        buttons.add(levelButton);

        GameObject info = new GameObject("InfoButton", new Transform(new Pair(965, 162)), 6);
        SceneChangerButton Info = new SceneChangerButton(60, 61, infoButton, 9, 6, "Level4");
        info.addComponent(Info);
        info.setUI(true);
        info.setNonserializable();
        buttons.add(info);

        GameObject Left = new GameObject("LeftArrow", new Transform(new Pair(50, 392)), 6);
        SceneChangerButton left = new SceneChangerButton(LeftArrowKey.width, LeftArrowKey.height, LeftArrowKey, LeftArrowKey, 5);
        Left.addComponent(left);
        levelButton.setUI(true);
        Left.setNonserializable();
        buttons.add(Left);

        GameObject Right = new GameObject("RightArrow", new Transform(new Pair(1200, 392)), 6);
        SceneChangerButton right = new SceneChangerButton(RightArrowKey.width, RightArrowKey.height, RightArrowKey, RightArrowKey, 3);
        Right.addComponent(right);
        levelButton.setUI(true);
        Right.setNonserializable();
        buttons.add(Right);

        GameObject BackButton = new GameObject("Back", new Transform(new Pair(1150, 50)), 10);
        SceneChangerButton back = new SceneChangerButton(70, 74, backButton, backButton, 2);
        BackButton.addComponent(back);
        BackButton.setUI(true);
        BackButton.setNonserializable();
        buttons.add(BackButton);

        addGameObject(levelButton);
        addGameObject(Left);
        addGameObject(Right);
        addGameObject(BackButton);
        addGameObject(info);
    }

    /**
     * Updates the scene.
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
     * Draws on the screen.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        g2.setColor(new Color(118f / 255.0f, 10f / 255.0f, 243f / 255.0f, 1.0f));
        g2.fillRect(0, Constants.MenuGround_Y, Constants.ScreenWidth, Constants.ScreenHeight - Constants.MenuGround_Y);

        renderer.render(g2);
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
