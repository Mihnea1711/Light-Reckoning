package com.Buttons;

import com.Components.Button;
import com.Components.Sprite;
import com.Game.DataBaseHandler;
import com.Game.Window;

import java.util.Objects;

import static com.main.Main.conn;

public class SceneChangerButton extends Button {
    private boolean importLvl;
    public int sceneIndex, prevSceneNumber;
    public String  filename, zipFilePath, musicFile, backgroundPath, groundPath;

    //for back button
    public SceneChangerButton(int width, int height, Sprite image, Sprite imageSelected, int sceneIndex) {
        super(width, height, image, imageSelected);
        this.sceneIndex = sceneIndex;
    }

    public SceneChangerButton(int width, int height, Sprite image, int sceneIndex, int prevSceneIndex, String filename) {
        super(width, height, image, image);
        this.sceneIndex = sceneIndex;
        this.filename = filename;
        this.prevSceneNumber = prevSceneIndex;
    }

    public SceneChangerButton(int width, int height, Sprite image, Sprite imageSelected, int sceneIndex, boolean importLvl) {
        super(width, height, image, imageSelected);
        this.sceneIndex = sceneIndex;
        this.importLvl = importLvl;
    }

    //for level buttons
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
