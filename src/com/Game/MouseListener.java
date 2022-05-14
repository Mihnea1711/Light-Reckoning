package com.Game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Extends Mouse adapter -> Window events registered, keeps track of mouse events.
 */
public class MouseListener extends MouseAdapter {
    public boolean mousePressed = false;            //if we press any button on the mouse
    public boolean mouseDragged = false;            //if we drag the mouse
    public int mouseButton = -1;                    //button

    //current pos of the mouse
    public float x = - 1.0f;
    public float y = -1.0f;

    //how much distance travelled since last frame
    public float dx = - 1.0f;
    public float dy = - 1.0f;

    /**
     * Sets the mouse pressed and button variables whenever the mouse is being pressed.
     * @param mouseEvent event for clicking the mouse.
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent){
        this.mousePressed = true;
        this.mouseButton = mouseEvent.getButton();
    }

    /**
     * Sets the variables for releasing the mouse. (stop pressing buttons)
     * @param mouseEvent event for clicking the mouse
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent){
        this.mousePressed = false;          //no longer being pressed
        this.mouseDragged = false;          //no longer being dragged
        //if no dragging then no distance to calculate..
        this.dx = 0;
        this.dy = 0;
    }

    /**
     * Sets the coordinates of the mouse while being moved.
     * Event is called whenever the mouse is moved on the window.
     * @param mouseEvent event for clicking the mouse
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent){
        this.x = mouseEvent.getX();
        this.y = mouseEvent.getY();
    }

    /**
     * Everytime the user is clicking and dragging, we update position and distance travelled.
     * @param mouseEvent event for clicking the mouse
     */
    @Override
    public void mouseDragged(MouseEvent mouseEvent){
        this.mouseDragged = true;
        this.dx = mouseEvent.getX() - this.x;       //distance travelled for x
        this.dy = mouseEvent.getY() - this.y;       //distance travelled for y
    }
}
