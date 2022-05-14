package com.Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Takes care of the key events
 */
public class KeyListener extends KeyAdapter implements java.awt.event.KeyListener {
    /**
     * contains pressed keyboard keys, 128 chars in ascii
     */
    private boolean keyPressed[] = new boolean[128];

    /**
     * Stores the keys that are currently pressed.
     * @param e event for the keyboard
     */
    @Override
    public void keyPressed(KeyEvent e){
        keyPressed[e.getKeyCode()] = true;
    }

    /**
     * Will let us know which keys are pressed and which are not.
     * @param e event for the keyboard
     */
    @Override
    public void keyReleased(KeyEvent e){
        keyPressed[e.getKeyCode()] = false;
    }

    /**
     * @param keyCode index of the key we want to know if it is pressed or not
     * @return  true/false whether the key is pressed or not
     */
    public boolean isKeyPressed(int keyCode){
        return keyPressed[keyCode];
    }
}
