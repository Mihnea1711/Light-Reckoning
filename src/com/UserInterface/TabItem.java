package com.UserInterface;

import com.Components.Sprite;
import com.Game.Component;
import com.Game.Window;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class TabItem extends Component {
    private int x, y, width, height;
    private Sprite sprite;
    public boolean isSelected;

    private int bufferX, bufferY;

    private MainContainer parentContainer;

    public TabItem(int x, int y, int width, int height, Sprite sprite, MainContainer parent) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.sprite = sprite;
        this.isSelected = false;
        this.parentContainer = parent;
    }

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

    @Override
    public void draw(Graphics2D g2) {
        if(isSelected) {
            g2.drawImage(sprite.img, x, y, width, height, null);
        } else {
            float alpha = 0.5f;
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2.setComposite(ac);
            g2.drawImage(sprite.img, x, y, width, height, null);
            alpha = 1.0f;
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2.setComposite(ac);
        }
    }

    @Override
    public String serialize(int tabSize) {
        return null;
    }

    @Override
    public Component copy() {
        return null;
    }
}
