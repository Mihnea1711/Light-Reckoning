package com.Components;

import com.Game.Component;
import com.Game.Window;

import java.awt.event.MouseEvent;

public class CameraControls extends Component {
    private float prevMouseX, prevMouseY;

    public CameraControls() {
        prevMouseX = 0.0f;
        prevMouseY = 0.0f;
    }

    @Override
    public void update(double dTime) {
        if(Window.getWindow().mouseListener.mousePressed && Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON2) {
            float dx = (Window.getWindow().mouseListener.x + Window.getWindow().mouseListener.dx - prevMouseX);
            float dy = (Window.getWindow().mouseListener.y + Window.getWindow().mouseListener.dy - prevMouseY);

            Window.getWindow().getCurrentScene().camera.pos.x -= dx;
            Window.getWindow().getCurrentScene().camera.pos.y -= dy;
        }

        prevMouseX = Window.getWindow().mouseListener.x + Window.getWindow().mouseListener.dx;
        prevMouseY = Window.getWindow().mouseListener.y + Window.getWindow().mouseListener.dy;
    }

    @Override
    public Component copy() {
        return null;
    }
}
