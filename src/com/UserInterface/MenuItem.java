package com.UserInterface;

import com.Components.SnapToGrid;
import com.Components.Sprite;
import com.Game.Component;
import com.Game.GameObject;
import com.Game.LevelEditorScene;
import com.Game.Window;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class MenuItem extends Component {
    private int x, y, width, height;
    private Sprite buttonSprite, hoverSprite, Selected;
    public boolean isSelected = false;

    private int bufferX, bufferY;

    public MenuItem(int x, int y, int width, int height, Sprite buttonSprite, Sprite hoverSprite) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.buttonSprite = buttonSprite;
        this.hoverSprite = hoverSprite;
    }

    @Override
    public void start() {       //called after the whole game object is constructed and has all its components attached
        Selected = gameObject.getComp(Sprite.class);
        //the img should be centred on the button
        this.bufferX = (int)((this.width / 2.0) - (Selected.width / 2.0));
        this.bufferY = (int)((this.height / 2.0) - (Selected.height / 2.0));
    }

    @Override
    public void update(double dTime) {
        if(!isSelected &&
           Window.getWindow().mouseListener.x > this.x && Window.getWindow().mouseListener.x <= this.x + this.width &&
           Window.getWindow().mouseListener.y > this.y && Window.getWindow().mouseListener.y <= this.y + this.height) {
            if(Window.getWindow().mouseListener.mousePressed && Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON1) {
                //clicked inside the button
                GameObject obj = gameObject.copy();
                obj.removeComponent(MenuItem.class);        //don t want to have the MenuItem class
                LevelEditorScene scene = (LevelEditorScene)Window.getWindow().getCurrentScene();

                SnapToGrid snapToGrid = scene.mouseCursor.getComp(SnapToGrid.class);
                obj.addComponent(snapToGrid);               //want to preserve the snapToGrid
                scene.mouseCursor = obj;
                isSelected = true;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(this.buttonSprite.img, this.x, this.y, this.width, this.height, null);
        g2.drawImage(Selected.img, this.x + bufferX, this.y + bufferY, Selected.width, Selected.height, null);
        if(isSelected) {
            g2.drawImage(hoverSprite.img, this.x, this.y, this.width, this.height, null);
        }
    }

    @Override
    public Component copy() {
        return null;
    }

    @Override
    public String serialize(int tabSize) {
        return "";
    }
}