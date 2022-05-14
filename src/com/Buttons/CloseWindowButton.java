package com.Buttons;

import com.Components.Button;
import com.Components.Sprite;

/**
 * Class for the exit button inside main menu.
 */
public class CloseWindowButton extends Button {

    /**
     * Constructor.
     * @param width button width
     * @param height button height
     * @param image button image
     */
    public CloseWindowButton(int width, int height, Sprite image) {
        super(width, height, image, image);
    }

    /**
     * Main function of the button.
     */
    @Override
    public void buttonPressed() {
        System.out.println("Exited the game...");
        System.exit(0);
    }
}
