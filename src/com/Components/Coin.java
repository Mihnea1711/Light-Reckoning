package com.Components;

import com.File.Parser;
import com.Game.*;
import com.Utilities.Constants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * Class for the coin.
 */
public class Coin extends Component {
    public boolean wasCollected;
    public GameObject player;
    public CircleBounds bounds;

    /**
     * Constructor.
     * @param wasCollected flag whether the coin was collected or not
     */
    public Coin(boolean wasCollected) {
        this.wasCollected = wasCollected;
    }

     /**
     * Constructor.
     * @param wasCollected flag whether the coin was collected or not
     * @param player player
     */
    public Coin(boolean wasCollected, GameObject player) {
        this.wasCollected = wasCollected;
        this.player = player;
    }

    /**
     * Utility function that gets called after the creation of an object.
     */
    @Override
    public void start(){
        this.bounds = gameObject.getComp(CircleBounds.class);
        Scene scene = Window.getScene();
        if(scene instanceof LevelScene) {       //checks if it is a level scene. will tell if we are in level scene or in editor
            LevelScene levelScene = (LevelScene)scene;
            this.player = levelScene.player;
        }
    }

    /**
     * Serializes the coin data.
     * @param tabSize   number of tabs to be indented correctly
     * @return the portal properties serialized
     */
    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("Coin", tabSize));
        builder.append(addBooleanProperty("wasCollected", this.wasCollected, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    /**
     * Deserializes the coin data.
     * @return a new portal object with the deserialized properties
     */
    public static Coin deserialize() {
        boolean wasCollected = Parser.consumeBooleanProperty("wasCollected");
        Parser.consumeEndObjectProperty();

        return new Coin(wasCollected);
    }

    /**
     * Update function for the coin.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if(player != null) {
            if(CircleBounds.checkCollision(bounds, player.getComp(Bounds.class)) && !this.wasCollected) {
                this.wasCollected = true;
                //DataBaseHandler.updateCoins(conn, Window.getScene().name);
            }
        }
    }


    /**
     * Draw method for the coin.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        if(wasCollected) {
            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{15}, 0));
            Shape circle = new Ellipse2D.Float(this.gameObject.getPosX() + bounds.xBuffer, this.gameObject.getPosY() + bounds.yBuffer,
                    bounds.getWidth(), bounds.getHeight());
            g2.draw(circle);
            g2.setStroke(Constants.Line);
        }
    }

    /**
     * Copy method for the coin.
     * @return a new object = copy of a Coin
     */
    @Override
    public Component copy() {
        return new Coin(this.wasCollected, this.player);
    }
}
