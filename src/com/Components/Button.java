package com.Components;

import com.Game.Component;
import com.Game.Window;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public abstract class Button extends Component {
    public int width, height;
    public Sprite Image, SelectedImage;

    public boolean isSelected = false;
    private int framesForAnimation = 3;
    private int framesLeft = 0;

    private float debounceTime = 0.1f;
    private float debounceLeft = 0.0f;

    public Button(int width, int height, Sprite Image, Sprite SelectedImage) {
        this.width = width;
        this.height = height;
        this.Image = Image;
        this.SelectedImage = SelectedImage;
    }

    public abstract void buttonPressed();

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

    @Override
    public void draw(Graphics2D g2) {
        if(!isSelected) {
            g2.drawImage(Image.img, (int)gameObject.getPosX(), (int)gameObject.getPosY(), width, height, null);
        } else {
            g2.drawImage(SelectedImage.img, (int)gameObject.getPosX(), (int)gameObject.getPosY(), width, height, null);
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
