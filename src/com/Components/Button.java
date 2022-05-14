package com.Components;

import com.Game.Component;
import com.Game.Window;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Abstract class of a button. Base class for the implemented buttons.
 */
public abstract class Button extends Component {
    public int width, height;
    public Sprite Image, SelectedImage;
    public String text;

    public boolean isSelected = false;
    private int framesForAnimation = 10;
    private int framesLeft = 0;

    private float debounceTime = 0.6f;
    private float debounceLeft = 0.0f;

    private int wrapLength, fontSize;
    private int xBuff, yBuff;

    /**
     * Constructor for the simple button.
     * @param width button width
     * @param height button height
     * @param Image button non-pressed image
     * @param SelectedImage button pressed image
     */
    public Button(int width, int height, Sprite Image, Sprite SelectedImage) {
        this.width = width;
        this.height = height;
        this.Image = Image;
        this.SelectedImage = SelectedImage;
    }

    /**
     * Constructor for text button.
     * Variables wrapLength and fontSize, along with xBuff and yBuff are used for centering the text inside the button.
     * @param width button width
     * @param height button height
     * @param Image button non-pressed image
     * @param SelectedImage button pressed image
     * @param text button text
     */
    public Button(int width, int height, Sprite Image, Sprite SelectedImage, String text) {
        this.width = width;
        this.height = height;
        this.Image = Image;
        this.SelectedImage = SelectedImage;
        this.text = text;

        this.fontSize = 35;
        this.wrapLength = 250;

        this.xBuff = (int)((width - wrapLength) / 2.0);
        this.yBuff = (int)((height - fontSize) / 2.0);
    }

    /**
     * Button update function.
     * Uses debounceLeft, debounceTime and framesLeft, framesForAnimation for the button behaviour.
     * Variable isSelected is a flag whether the button is selected or not.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if(debounceLeft > 0) debounceLeft -= dTime;

        if(Window.getWindow().mouseListener.mousePressed &&
           Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON1 &&
           debounceLeft <= 0.0f) {
            debounceLeft = debounceTime;
            if (Window.getWindow().mouseListener.x > this.gameObject.getPosX() &&
                Window.getWindow().mouseListener.x <= this.gameObject.getPosX() + width &&
                Window.getWindow().mouseListener.y > this.gameObject.getPosY() &&
                Window.getWindow().mouseListener.y <= this.gameObject.getPosY() + height) {
                    //clicked inside the button
                    this.buttonPressed();
                    isSelected = true;
                    framesLeft = framesForAnimation;
            }
        }

        if(isSelected && framesLeft > 0) {
            framesLeft--;
        } else if(isSelected && framesLeft == 0) {
            isSelected = false;
        }
    }

    /**
     * Utility function for drawing the text inside the button.
     * @param fm font metrics
     * @param g2 graphics handler
     */
    private void drawTextWrapped(FontMetrics fm, Graphics2D g2) {
        int lineHeight = fm.getHeight();

        int startX = (int)this.gameObject.getPosX() + xBuff;
        int currentX = startX;
        int currentY = (int)this.gameObject.getPosY() + yBuff + 20;

        for(char c : text.toCharArray()) {
            if(c == '\n') {
                currentX = startX;
                currentY += lineHeight;
                continue;
            }
            g2.drawString("" + c, currentX, currentY);

            currentX += fm.stringWidth("" + c);
            if(currentX >= startX + wrapLength) {
                currentX = startX;
                currentY += lineHeight;
            }
        }
    }

    /**
     * Button draw function.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        g2.setFont(new Font("Calibri", Font.PLAIN, fontSize));
        FontMetrics fm = g2.getFontMetrics();

        if(!isSelected) {
            g2.drawImage(Image.img, (int)gameObject.getPosX(), (int)gameObject.getPosY(), width, height, null);
        } else {
            g2.drawImage(SelectedImage.img, (int)gameObject.getPosX(), (int)gameObject.getPosY(), width, height, null);
        }
        if(text != null) {
            drawTextWrapped(fm, g2);
        }
    }

    /**
     * No need for implementation.
     * @param tabSize number of tabs to be indented correctly
     * @return nothing
     */
    @Override
    public String serialize(int tabSize) {
        return null;
    }

    /**
     * No need for implementation.
     * @return nothing
     */
    @Override
    public Component copy() {
        return null;
    }

    /**
     * Main function of the button.
     * Will be overridden by the subclasses.
     */
    public abstract void buttonPressed();
}
