package com.Game;

import com.Components.*;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.Utilities.Constants;
import com.Utilities.Pair;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the Main Menu
 */
public class MainMenuScene extends Scene {
    private GameObject mouseCursor;

    private List<GameObject> buttons;

    private Sprite ButtonPlay, ButtonPlayPressed, ButtonEditor, ButtonEditorPressed;

    private Sprite logo0, logo1, logo2, logo3;

    private Sprite Title;

    /**
     * Constructor
     * @param name name of menu
     */
    public MainMenuScene(String name) {
        super.Scene(name);
        this.buttons = new ArrayList<>();
    }

    /**
     * Initialization method
     */
    public void init() {
        initAssetPool();
        mouseCursor = new GameObject("Mouse Cursor", new Transform(new Pair()), 10);
        GameObject player = new GameObject("Player", new Transform(new Pair(Constants.MenuPlayerX, Constants.MenuPlayerY)), 0);
        SpriteSheet layer1 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerOne.png");
        SpriteSheet layer2 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerTwo.png");
        SpriteSheet layer3 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerThree.png");
        Player playerComps = new Player(layer1.sprites.get(20), layer2.sprites.get(20), layer3.sprites.get(20), Color.RED, Color.GREEN);
        player.addComponent(playerComps);

        addGameObject(player);

        initBackGrounds();
        initButtons();
    }

    public void initAssetPool() {
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerOne.png", Constants.PlayerWidth, Constants.PlayerHeight, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerTwo.png", Constants.PlayerWidth, Constants.PlayerHeight, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerThree.png", Constants.PlayerWidth, Constants.PlayerHeight, 2, 13, 13*5);

        AssetPool.addSpritesheet("Assets/MainMenu/Buttons/play.png", 121, 121, 0, 1, 1);
        AssetPool.addSpritesheet("Assets/MainMenu/Buttons/editor.png", 126, 115, 0, 1, 1);
        AssetPool.addSpritesheet("Assets/MainMenu/Buttons/playPressed.png", 121, 121, 0, 1, 1);
        AssetPool.addSpritesheet("Assets/MainMenu/Buttons/editorPressed.png", 126, 115, 0, 1, 1);

        AssetPool.addSpritesheet("Assets/MainMenu/Logos/LogoLevel0.png", Constants.LogoSize, Constants.LogoSize, 0, 1, 1);
        AssetPool.addSpritesheet("Assets/MainMenu/Logos/LogoLevel1.png", Constants.LogoSize, Constants.LogoSize, 0, 1, 1);
        AssetPool.addSpritesheet("Assets/MainMenu/Logos/LogoLevel2.png", Constants.LogoSize, Constants.LogoSize, 0, 1, 1);
        AssetPool.addSpritesheet("Assets/MainMenu/Logos/LogoLevel3.png", Constants.LogoSize, Constants.LogoSize, 0, 1, 1);

        AssetPool.addSpritesheet("Assets/MainMenu/title.png", 800, 91, 0, 1, 1);

        AssetPool.addSpritesheet("Assets/spikes.png", Constants.TileWidth, Constants.TileHeight, 2, 6, 4);

        this.ButtonPlay = AssetPool.getSprite("Assets/MainMenu/Buttons/play.png");
        this.ButtonEditor = AssetPool.getSprite("Assets/MainMenu/Buttons/editor.png");
        this.ButtonPlayPressed = AssetPool.getSprite("Assets/MainMenu/Buttons/playPressed.png");
        this.ButtonEditorPressed = AssetPool.getSprite("Assets/MainMenu/Buttons/editorPressed.png");

        this.logo0 = AssetPool.getSprite("Assets/MainMenu/Logos/LogoLevel0.png");
        this.logo1 = AssetPool.getSprite("Assets/MainMenu/Logos/LogoLevel1.png");
        this.logo2 = AssetPool.getSprite("Assets/MainMenu/Logos/LogoLevel2.png");
        this.logo3 = AssetPool.getSprite("Assets/MainMenu/Logos/LogoLevel3.png");

        this.Title = AssetPool.getSprite("Assets/MainMenu/title.png");
    }

    public void initButtons() {

        GameObject play = new GameObject("SceneChangerButton", new Transform(new Pair(Constants.MenuPlayButtonX, Constants.MenuPlayButtonY)), 10);
        SceneChangerButton playButton = new SceneChangerButton(ButtonPlay.width, ButtonPlay.height, ButtonPlay, ButtonPlayPressed, 1);
        play.addComponent(playButton);
        play.setUI(true);
        play.setNonserializable();
        buttons.add(play);

        GameObject editor = new GameObject("SceneChangerButton", new Transform(new Pair(Constants.MenuEditorButtonX, Constants.MenuEditorButtonY)), 10);
        SceneChangerButton editorButton = new SceneChangerButton(ButtonEditor.width, ButtonEditor.height, ButtonEditor, ButtonEditorPressed, 0);
        editor.addComponent(editorButton);
        editor.setUI(true);
        editor.setNonserializable();
        buttons.add(editor);

        addGameObject(play);
        addGameObject(editor);
    }

    /**
     * Initialization method for backgrounds
     */
    public void initBackGrounds() {
        GameObject ground = new GameObject("Ground", new Transform(new Pair(0, Constants.MenuGround_Y)), 1);
        ground.addComponent(new Ground());
        ground.setNonserializable();
        addGameObject(ground);

        GameObject noob = new GameObject("Noob", new Transform(new Pair(400, 289)), -5);
        noob.addComponent(new Sprite(logo0.img, "Assets/MainMenu/Logos/LogoLevel0.png"));
        noob.setNonserializable();
        addGameObject(noob);

        GameObject normal = new GameObject("Normal", new Transform(new Pair(430, 250)), -5);
        normal.addComponent(new Sprite(logo1.img, "Assets/MainMenu/Logos/LogoLevel1.png"));
        normal.setNonserializable();
        addGameObject(normal);

        GameObject hard = new GameObject("Hard", new Transform(new Pair(480, 230)), -5);
        hard.addComponent(new Sprite(logo2.img, "Assets/MainMenu/Logos/LogoLevel2.png"));
        hard.setNonserializable();
        addGameObject(hard);

        GameObject demon = new GameObject("Demon", new Transform(new Pair(530, 220)), -5);
        demon.addComponent(new Sprite(logo3.img, "Assets/MainMenu/Logos/LogoLevel3.png"));
        demon.setNonserializable();
        addGameObject(demon);

        GameObject title = new GameObject("Title", new Transform(new Pair(240, 50)), -5);
        title.addComponent(new Sprite(Title.img, "Assets/MainMenu/title.png"));
        title.setNonserializable();
        addGameObject(title);

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
        for (GameObject obj : buttons) {
            obj.update(dTime);
        }

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
