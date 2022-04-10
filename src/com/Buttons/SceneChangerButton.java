package com.Buttons;

import com.Components.Button;
import com.Components.Sprite;
import com.Game.Window;

public class SceneChangerButton extends Button {
    private boolean importLvl;
    public int sceneIndex;
    public String  filename, zipFilePath, musicFile, backgroundPath, groundPath;

    //for back button
    public SceneChangerButton(int width, int height, Sprite image, Sprite imageSelected, int sceneIndex) {
        super(width, height, image, imageSelected);
        this.sceneIndex = sceneIndex;
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
        Window.getWindow().changeScene(sceneIndex, filename, zipFilePath, musicFile, backgroundPath, groundPath, importLvl);
    }
}
