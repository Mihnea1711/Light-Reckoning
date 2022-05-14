package com.Components;

import com.Game.Component;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Class for the music file.
 */
public class Music extends Component {

    private String filepath;
    AudioInputStream audio = null;
    Clip audioClip = null;

    /**
     * Constructor.
     * @param filename name of the audio file
     */
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

    /**
     * Function to stop the current clip.
     */
    public void stop() {
        audioClip.stop();
    }

    /**
     * Function to restart the clip.
     */
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

    /**
     * Serialize method for the music file (no need).
     * @return nothing
     */
    @Override
    public String serialize(int tabSize) {
        return null;
    }

    /**
     * Copy method for the music file (no need).
     * @return nothing
     */
    @Override
    public Component copy() {
        return null;
    }
}
