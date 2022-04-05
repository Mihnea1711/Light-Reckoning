package com.Buttons;

import com.Components.Button;
import com.Components.Sprite;
import com.Game.Window;

public class SceneChangerButton extends Button {

    public int sceneIndex;
    public String filename, musicFile;

    public SceneChangerButton(int width, int height, Sprite image, Sprite imageSelected, int sceneIndex) {
        super(width, height, image, imageSelected);
        this.sceneIndex = sceneIndex;
    }

    public SceneChangerButton(int width, int height, Sprite image, Sprite imageSelected, String text, int sceneIndex, String filename, String musicFile) {
        super(width, height, image, imageSelected, text);
        this.sceneIndex = sceneIndex;
        this.filename = filename;
        this.musicFile = musicFile;
    }

    @Override
    public void buttonPressed() {
        if(Window.getMusic() != null) {
            Window.getMusic().stop();
        }
        Window.getWindow().changeScene(sceneIndex, filename, musicFile);
    }
}
