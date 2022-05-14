package com.Game;

import com.Buttons.SceneChangerButton;
import com.Buttons.SubmitButton;
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
import java.util.Scanner;

/**
 * The scene where we create a new level, or where we type the level name to import it.
 */
public class CreateNewLevelMenu extends Scene {
    //private TextField textField;
    private SubmitButton button;
    private GameObject mouseCursor;

    /**
     * list of buttons inside the scene
     */
    private List<GameObject> buttons;

    /**
     * this is temporary (used to enter te level name)
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * flag for the text required
     */
    boolean entered = false;

    /**
     * flag whether we want to import a level or create a new one
     */
    boolean importLVL;

    private Sprite backButton, submit, text;

    /**
     * Constructor.
     * @param name name of the scene
     */
    public CreateNewLevelMenu(String name) {
        super.Scene(name);
        this.buttons = new ArrayList<>();
        //this.textField = new TextField(575, 225, 300, 50);
    }

    /**
     * Initialization method.
     * 5 empty params to match the init function parameters.
     * @param a param1 - empty
     * @param b param2 - empty
     * @param c param3 - empty
     * @param d param4 - empty
     * @param e param5 - empty
     * @param importLVL flag
     */
    public void init(String a, String b, String c, String d, String e, boolean importLVL) {
        initAssetPool();
        mouseCursor = new GameObject("Mouse Cursor", new Transform(new Pair()), 10);
        initBackGrounds();
        initButtons();
        //Window.getWindow().addTextField(textField);
        entered = false;
        this.importLVL = importLVL;
    }

    /**
     * Initialization of the asset pool of the scene.
     */
    public void initAssetPool() {
        AssetPool.addSpritesheet("Assets/Global/back.png", 70, 74, 0,1, 1);
        AssetPool.addSpritesheet("Assets/UI/text.png", 150, 50, 0,1, 1);
        AssetPool.addSpritesheet("Assets/UI/submit.png", 50, 51, 0,1, 1);

        this.backButton = AssetPool.getSprite("Assets/Global/back.png");
        this.text = AssetPool.getSprite("Assets/UI/text.png");
        this.submit = AssetPool.getSprite("Assets/UI/submit.png");
    }

    /**
     * Initialization of the scene's backgrounds.
     */
    public void initBackGrounds() {
        GameObject ground = new GameObject("Ground", new Transform(new Pair(0, Constants.MenuGround_Y)), 1);
        ground.addComponent(new Ground());
        ground.setNonserializable();
        addGameObject(ground);

        GameObject levelNameText = new GameObject("levelName", new Transform(new Pair(415, 250)), 10);
        levelNameText.addComponent(new Sprite(text.img, "Assets/UI/text.png"));
        levelNameText.setNonserializable();
        addGameObject(levelNameText);

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
        GameObject BackButton = new GameObject("Back", new Transform(new Pair(1100, 50)), 10);
        SceneChangerButton back = new SceneChangerButton(70, 74, backButton, backButton, 8);
        BackButton.addComponent(back);
        BackButton.setUI(true);
        BackButton.setNonserializable();
        buttons.add(BackButton);

        GameObject submitButton = new GameObject("Submit", new Transform(new Pair(920, 250)), 10);
        button = new SubmitButton(75, 76, submit, submit, "");
        submitButton.addComponent(button);
        submitButton.setUI(true);
        submitButton.setNonserializable();
        buttons.add(submitButton);

        addGameObject(BackButton);
        addGameObject(submitButton);
    }

    /**
     * Updates the scene.
     * @param dTime keeps track of frames
     */
    @Override
    public void update(double dTime) {
        if(!entered) {
            System.out.println("Created Levels Available: " + levelsCreated);

            if (!importLVL || !levelsCreated.isEmpty()) {
                System.out.println("Enter level name: ");
                String levelName = scanner.nextLine();
                if (!levelsCreated.contains(levelName)) {
                    addCreatedLevel(levelName);
                    button.setCreateTrue();
                } else {
                    button.setCreateFalse();
                }
                button.setTextAttached(levelName);
            }
            //button.setTextAttached(textField.getText());
            entered = true;
        }

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
     * @param filename the file from where we take our serialized level.
     */
    @Override
    protected void importLvl(String filename, String zipFilePath) {

    }
}
