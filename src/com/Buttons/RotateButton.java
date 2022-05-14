package com.Buttons;

import com.Components.Button;
import com.Components.Sprite;

//TODO:: add correct implementation
/**
 * Class for the rotate buttons inside level editor controls.
 */
public class RotateButton extends Button {
    /**
     * Flag for rotation direction.
     */
    private final boolean rotateRight;

    /**
     * Constructor for the rotate button inside the editor.
     * @param width width of the button
     * @param height height of the button
     * @param image button image when non-clicked
     * @param imageSelected button image when clicked
     * @param text button text
     * @param rotateRight flag for direction of the rotation
     */
    public RotateButton(int width, int height, Sprite image, Sprite imageSelected, String text, boolean rotateRight) {
        super(width, height, image, imageSelected, text);
        this.rotateRight = rotateRight;
    }

    /**
     * Button main function.
     */
    @Override
    public void buttonPressed() {
        if(rotateRight) {
            //rotate 90*
        }
        else {
            //rotate -90*
        }
    }

    /**
     * Copy function for the button, so we don't pass references around.
     * @return a new rotate button with the same properties.
     */
    @Override
    public RotateButton copy() {
        return new RotateButton(width, height, Image, SelectedImage, text, rotateRight);
    }
}
