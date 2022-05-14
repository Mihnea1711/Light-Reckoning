package com.Buttons;

import com.Components.Button;
import com.Components.Sprite;
import com.Game.DataBaseHandler;
import com.Game.Window;

import java.util.Objects;

import static com.main.Main.conn;

/**
 * Class for the scene changer button.
 */
public class SceneChangerButton extends Button {

    /**
     * Flag whether we want to import a level or not.
     */
    private boolean importLvl;

    /**
     * Current scene index.
     */
    public int sceneIndex;

    /**
     * Previous scene index.
     */
    public int prevSceneNumber;

    /**
     * Current file name.
     */
    public String filename;

    /**
     * Current zip file name.
     */
    public String zipFilePath;

    /**
     * Current music file name.
     */
    public String musicFile;

    /**
     * Current background path name.
     */
    public String backgroundPath;

    /**
     * Current ground path name.
     */
    public String groundPath;

    /**
     * Constructor for the scene changer button. (back, arrows, main menu buttons)
     * @param width width of the button
     * @param height height of the button
     * @param image button image when non-clicked
     * @param imageSelected button image when clicked
     * @param sceneIndex index of the next scene
     */
    public SceneChangerButton(int width, int height, Sprite image, Sprite imageSelected, int sceneIndex) {
        super(width, height, image, imageSelected);
        this.sceneIndex = sceneIndex;
    }

    /**
     * Constructor for the scene changer button. (for the info buttons)
     * @param width width of the button
     * @param height height of the button
     * @param image button image when non-clicked
     * @param sceneIndex index of the next scene
     * @param prevSceneIndex index of the current scene
     * @param filename name of the file that has been modified
     */
    public SceneChangerButton(int width, int height, Sprite image, int sceneIndex, int prevSceneIndex, String filename) {
        super(width, height, image, image);
        this.sceneIndex = sceneIndex;
        this.filename = filename;
        this.prevSceneNumber = prevSceneIndex;
    }

    /**
     * Constructor for the scene changer button. (playCreated/new buttons)
     * @param width width of the button
     * @param height height of the button
     * @param image button image when non-clicked
     * @param imageSelected button image when clicked
     * @param sceneIndex index of the next scene
     * @param importLvl flag whether we want to import the level or not
     */
    public SceneChangerButton(int width, int height, Sprite image, Sprite imageSelected, int sceneIndex, boolean importLvl) {
        super(width, height, image, imageSelected);
        this.sceneIndex = sceneIndex;
        this.importLvl = importLvl;
    }

    /**
     * Constructor for level buttons.
     * @param width button width
     * @param height button height
     * @param image button image (non-pressed)
     * @param imageSelected button image (pressed)
     * @param text button text
     * @param sceneIndex index of the next scene
     * @param filename name of the file to be imported
     * @param zipFilePath name of the file's zip
     * @param musicFile song's path
     * @param backgroundPath background's path
     * @param groundPath ground's path
     */
    public SceneChangerButton(int width, int height, Sprite image, Sprite imageSelected, String text, int sceneIndex,
                              String filename, String zipFilePath, String musicFile, String backgroundPath, String groundPath) {
        super(width, height, image, imageSelected, text);
        this.sceneIndex = sceneIndex;
        this.filename = filename;
        this.musicFile = musicFile;
        this.backgroundPath = backgroundPath;
        this.groundPath = groundPath;
        this.zipFilePath = zipFilePath;
    }

    /**
     * Main function of the button.
     */
    @Override
    public void buttonPressed() {
        if(Window.getMusic() != null) {
            Window.getMusic().stop();
        }
        if((Objects.equals(filename, "Level1") || Objects.equals(filename, "Level2") ||
                Objects.equals(filename, "Level3") || Objects.equals(filename, "Level4")) && sceneIndex == 1) {
            DataBaseHandler.updateAttempts(conn, filename);
        }
        Window.getWindow().changeScene(sceneIndex, prevSceneNumber, filename, zipFilePath, musicFile, backgroundPath, groundPath, importLvl);
    }
}
