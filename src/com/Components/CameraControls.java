package com.Components;

import com.Game.Component;
import com.Game.Window;
import com.Utilities.Constants;

import java.awt.event.MouseEvent;

/**
 * Class to control to camera movement.
 */
public class CameraControls extends Component {

    /**
     * Last x position of the mouse.
     */
    private float prevMouseX;

    /**
     * Last y position of the mouse.
     */
    private float prevMouseY;

    /**
     * Constructor for camera controls.
     */
    public CameraControls() {
        prevMouseX = 0.0f;
        prevMouseY = 0.0f;
    }

    /**
     * Keeps track of the movement of the camera when middle button is pressed.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if(Window.getWindow().mouseListener.mousePressed && Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON2) {
            //how far we moved the mouse
            float dx = (Window.getWindow().mouseListener.x + Window.getWindow().mouseListener.dx - prevMouseX);
            float dy = (Window.getWindow().mouseListener.y + Window.getWindow().mouseListener.dy - prevMouseY);

            //move the camera position
            Window.getWindow().getCurrentScene().camera.pos.x -= dx;
            Window.getWindow().getCurrentScene().camera.pos.y -= dy;

            if (Window.getWindow().getCurrentScene().camera.pos.y > Constants.CameraY + 30)
                Window.getWindow().getCurrentScene().camera.pos.y = Constants.CameraY + 30;
        }

        //update the mouse position
        prevMouseX = Window.getWindow().mouseListener.x + Window.getWindow().mouseListener.dx;
        prevMouseY = Window.getWindow().mouseListener.y + Window.getWindow().mouseListener.dy;
    }

    /**
     * Copy method.
     * No need for implementation.
     * @return nothing
     */
    @Override
    public Component copy() {
        return null;
    }

    /**
     * Don't need to save, we will be building it anyway.
     * @param tabSize number of tabs to be indented correctly
     * @return nothing
     */
    @Override
    public String serialize(int tabSize) {
        return "";
    }
}
