package com.UserInterface;

import com.Components.Sprite;
import com.Game.Component;
import com.Game.Window;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Very similar to MenuItem class.
 * Takes care of the hot tabs.
 */
public class TabItem extends Component {
    private int x, y, width, height;
    private Sprite sprite;
    public boolean isSelected;

    private MainContainer parentContainer;

    /**
     * Constructor.
     * @param x x position of button
     * @param y y position of button
     * @param width width of button
     * @param height height of button
     * @param sprite img sprite for the tab
     * @param parent parent container
     */
    public TabItem(int x, int y, int width, int height, Sprite sprite, MainContainer parent) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.sprite = sprite;
        this.isSelected = false;
        this.parentContainer = parent;
    }

    /**
     * Updates and keeps track of the hot tabs.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if(Window.getWindow().mouseListener.mousePressed && Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON1) {
            if (!isSelected &&
                Window.getWindow().mouseListener.x > this.x && Window.getWindow().mouseListener.x <= this.x + this.width &&
                Window.getWindow().mouseListener.y > this.y && Window.getWindow().mouseListener.y <= this.y + this.height) {
                    isSelected = true;
                    this.parentContainer.setHotTab(gameObject);
            }
        }
    }

    /**
     * Draws the tabs half transparent if not selected and full transparent otherwise.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        if(isSelected) {
            g2.drawImage(sprite.img, x, y, width, height, null);
        } else {
            //half transparency
            float alpha = 0.5f;
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2.setComposite(ac);
            g2.drawImage(sprite.img, x, y, width, height, null);
            //full transparency
            alpha = 1.0f;
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2.setComposite(ac);
        }
    }

    /**
     * Don't need to save, we will be building it anyway.
     * @param tabSize number of tabs to be indented correctly
     * @return nothing
     */
    @Override
    public String serialize(int tabSize) {
        return null;
    }

    /**
     * Don't need to implement this.
     * @return nothing
     */
    @Override
    public Component copy() {
        return null;
    }
}
