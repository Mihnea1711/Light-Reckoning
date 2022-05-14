package com.Components;

import com.Game.Camera;
import com.Game.Component;
import com.Game.Window;
import com.Utilities.Constants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * Contains the grid.
 */
public class Grid extends Component {

    Camera camera;  //reference to the camera
    public int gridWidth, gridHeight;       //individual tiles in the grid width/height
    private int numYLines = Constants.GridYLines;
    private int numXLines = Constants.GridXLines;

    /**
     * Constructor for the grid.
     */
    public Grid() {
        this.camera = Window.getWindow().getCurrentScene().camera;
        this.gridHeight = Constants.TileHeight;
        this.gridWidth = Constants.TileWidth;
    }

    /**
     * Update method for the grid (no need).
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {

    }

    /**
     * Draws the grid, so we have 42x42 squares going across the screen.
     * This won't be submitted to the renderer, instead will be drawn directly.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1f));      //the stroke 1 pixel wide
        g2.setColor(new Color(0.3f, 0.3f, 0.9f, 0.5f));     //light and halfway transparent

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

    /**
     * Copy method of the grid (no need).
     * @return nothing
     */
    @Override
    public Component copy() {
        return null;
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
