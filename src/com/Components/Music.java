package com.Components;

import com.Game.Component;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music extends Component {

    private String filepath;
    AudioInputStream audio = null;
    Clip audioClip = null;

    public Music(String filename) {
        this.filepath = new File(filename).getAbsolutePath();

        try {
            audio = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());
            audioClip = AudioSystem.getClip();
            audioClip.open(audio);
            restartClip();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void stop() {
        audioClip.stop();
    }

    public void restartClip() {
        try {
            audioClip.stop();
            audioClip.setFramePosition(0);
            audioClip.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public String serialize(int tabSize) {
        return null;
    }

    @Override
    public Component copy() {
        return null;
    }
}
