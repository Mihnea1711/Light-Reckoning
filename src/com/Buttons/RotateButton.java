package com.Buttons;

import com.Components.Button;
import com.Components.Sprite;

/**
 * Class for the rotate buttons inside level editor controls
 */
public class RotateButton extends Button {

    private boolean rotateRight;

    public RotateButton(int width, int height, Sprite image, Sprite imageSelected, String text, boolean rotateRight) {
        super(width, height, image, imageSelected, text);
        this.rotateRight = rotateRight;
    }

    @Override
    public void buttonPressed() {
        if(rotateRight) {
            //rotate 90*
        }
        else {
            //rotate -90*
        }
    }

    @Override
    public RotateButton copy() {
        return new RotateButton(width, height, Image, SelectedImage, text, rotateRight);
    }
}
