package com.Components;

import com.Game.Component;
import com.Game.GameObject;
import com.Game.Window;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;


public class SnapToGrid extends Component {

    private float debounceTime = 0.1f;      //every 0.1 sec we will register one click -> no spamming blocks
    private float debounceLeft = 0.0f;
    int gridWidth, gridHeight;

    public SnapToGrid(int gridWidth, int gridHeight) {
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
    }

    @Override
    public void update(double dTime) {
        debounceLeft -= dTime;
        if(this.gameObject.getComp(Sprite.class) != null) {
            float x = (float)Math.floor((Window.getWindow().mouseListener.x + Window.getWindowCamX() + Window.getWindow().mouseListener.dx) / gridWidth);
            float y = (float)Math.floor((Window.getWindow().mouseListener.y + Window.getWindowCamY() + Window.getWindow().mouseListener.dy) / gridHeight);

            this.gameObject.transform.pos.x = x * gridWidth - Window.getWindowCamX();       //transforms it to be local to the window
            this.gameObject.transform.pos.y = y * gridHeight - Window.getWindowCamY();

            if(Window.getWindow().mouseListener.y < Constants.TabOffY &&
               y * gridHeight < Constants.GroundY &&
               Window.getWindow().mouseListener.mousePressed &&
               Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON1 && debounceLeft < 0) {
                debounceLeft = debounceTime;
                GameObject obj = gameObject.copy();
                obj.transform.pos = new TwoPair(x * gridWidth, y * gridHeight);
                Window.getWindow().getCurrentScene().addGameObject(obj);
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        Sprite sprite = gameObject.getComp(Sprite.class);           //we are having it as a game object cuz we will add several components to this
        if(sprite != null) {                                        //so when we copy it to the screen it will come with all of its properties
            float alpha = 0.5f;         //a little transparent before placing
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);     //apply the alpha over the entire image
            g2.setComposite(ac);
            g2.drawImage(sprite.img, (int)gameObject.getPosX(), (int)gameObject.getPosY(), (int)sprite.width, (int)sprite.height, null);
            alpha = 1.0f;                                                           //set it to full transparency
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);        //draw it non transparent
            g2.setComposite(ac);
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
