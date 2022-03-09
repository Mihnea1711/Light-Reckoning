package com.Game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyListener extends KeyAdapter implements java.awt.event.KeyListener {
    private boolean keyPressed[] = new boolean[128]; //contains pressed keyboard keys, 128 chars in ascii

    @Override
    public void keyPressed(KeyEvent e){
        keyPressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e){
        keyPressed[e.getKeyCode()] = false;
    }

    public boolean isKeyPressed(int keyCode){
        return keyPressed[keyCode];
    }
}
