package com.UserInterface;

import com.Components.LevelEditorControls;
import com.Components.Sprite;
import com.Game.Component;
import com.Game.GameObject;
import com.Game.LevelEditorScene;
import com.Game.Window;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Contains each of the components attached to the main container.
 */
public class MenuItem extends Component {
    private int x, y, width, height;
    private Sprite buttonSprite, hoverSprite, Selected;
    public boolean isSelected;

    private int bufferX, bufferY;       //variables for centring the sprite inside the button

    private MainContainer parentContainer;      //reference to the main container class

    /**
     * Constructor.
     * @param x x position of button
     * @param y y position of button
     * @param width     width of button
     * @param height    height of button
     * @param buttonSprite  button-not-clicked sprite
     * @param hoverSprite   button-clicked sprite
     * @param parent parent container
     */
    public MenuItem(int x, int y, int width, int height, Sprite buttonSprite, Sprite hoverSprite, MainContainer parent) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.buttonSprite = buttonSprite;
        this.hoverSprite = hoverSprite;
        this.isSelected = false;
        this.parentContainer = parent;
    }

    /**
     * Constructor.
     * @param x x position of button
     * @param y y position of button
     * @param width     width of button
     * @param height    height of button
     * @param buttonSprite  button-not-clicked sprite
     * @param hoverSprite   button-clicked sprite
     */
    public MenuItem(int x, int y, int width, int height, Sprite buttonSprite, Sprite hoverSprite) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.buttonSprite = buttonSprite;
        this.hoverSprite = hoverSprite;
        this.isSelected = false;
    }

    /**
     * It is called after the whole game object is constructed and has all its components attached.
     */
    @Override
    public void start() {
        Selected = gameObject.getComp(Sprite.class);
        //the img should be centred on the button
        this.bufferX = (int)((this.width / 2.0) - (Selected.width / 2.0));
        this.bufferY = (int)((this.height / 2.0) - (Selected.height / 2.0));
    }

    /**
     * Updates the menu items.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if(Window.getWindow().mouseListener.mousePressed && Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON1) {
            if (!isSelected &&
                Window.getWindow().mouseListener.x > this.x && Window.getWindow().mouseListener.x <= this.x + this.width &&
                Window.getWindow().mouseListener.y > this.y && Window.getWindow().mouseListener.y <= this.y + this.height) {
                //clicked inside the button
                    GameObject obj = gameObject.copy();         //copied the object to the object
                    obj.removeComponent(MenuItem.class);        //don't want to have the MenuItem class
                    LevelEditorScene scene = (LevelEditorScene) Window.getWindow().getCurrentScene();

                    LevelEditorControls levelEditorControls = scene.mouseCursor.getComp(LevelEditorControls.class);
                    obj.addComponent(levelEditorControls);               //want to preserve the levelEditorControls
                    scene.mouseCursor = obj;                //we got the object from menu items inside the mouse cursor
                    isSelected = true;
                    this.parentContainer.setHotButton(gameObject);
            }
        }

        //if we press escape, any button will be unselected
        if(Window.keyListener().isKeyPressed(KeyEvent.VK_ESCAPE)) {
            isSelected = false;
        }
    }

    /**
     * Draws the buttons.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(this.buttonSprite.img, this.x, this.y, this.width, this.height, null);
        g2.drawImage(this.Selected.img, this.x + bufferX, this.y + bufferY, Selected.width, Selected.height, null);
        if(isSelected) {
            g2.drawImage(hoverSprite.img, this.x, this.y, this.width, this.height, null);
        }
    }

    /**
     * Creates a new object instead of passing a reference around.
     * @return a new object with the same properties
     */
    @Override
    public MenuItem copy() {
        return new MenuItem(this.x, this.y, this.width, this.height,
                (Sprite)this.buttonSprite.copy(), (Sprite)this.hoverSprite.copy(), parentContainer);
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
