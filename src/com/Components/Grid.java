package com.Components;

import com.Game.Camera;
import com.Game.Component;
import com.Game.Window;
import com.Utilities.Constants;

import java.awt.*;
import java.awt.geom.Line2D;

public class Grid extends Component {

    Camera camera;
    public int gridWidth, gridHeight;
    private int numYLines = 31;
    private int numXLines = 20;

    public Grid() {
        this.camera = Window.getWindow().getCurrentScene().camera;
        this.gridHeight = Constants.TileHeight;
        this.gridWidth = Constants.TileWidth;
    }

    @Override
    public void update(double dTime) {

    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1f));      //the stroke 1 pixel wide
        g2.setColor(new Color(0.3f, 0.3f, 0.9f, 0.5f));

        float bottom = Math.min(Constants.GroundY - camera.getPosY(), Constants.ScreenHeight);  //bottom of the screen
        float startX = (float)Math.floor(camera.getPosX() / gridWidth) * gridWidth - camera.getPosX();      //first pos to draw the lines
        float startY = (float)Math.floor(camera.getPosY() / gridHeight) * gridHeight - camera.getPosY();

        for(int column = 0; column <= numYLines; column++){     //draw lines starting from startX until the end
            g2.draw(new Line2D.Float(startX, 0, startX, bottom));
            startX += gridWidth;
        }

        for(int row = 0; row <= numXLines; row++) {
            if(camera.getPosY() + startY < Constants.GroundY) {
                g2.draw(new Line2D.Float(0, startY, Constants.ScreenWidth, startY));
                startY += gridHeight;
            }
        }
    }
}
