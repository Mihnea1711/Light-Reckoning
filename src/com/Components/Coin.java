package com.Components;

import com.File.Parser;
import com.Game.*;
import com.Utilities.Constants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Coin extends Component {
    public boolean wasCollected;
    public GameObject player;
    public CircleBounds bounds;

    public Coin(boolean wasCollected) {
        this.wasCollected = wasCollected;
    }

    public Coin(boolean wasCollected, GameObject player) {
        this.wasCollected = wasCollected;
        this.player = player;
    }

    @Override
    public void start(){
        this.bounds = gameObject.getComp(CircleBounds.class);
        Scene scene = Window.getScene();
        if(scene instanceof LevelScene) {       //checks if it is a level scene. will tell if we are in level scene or in editor
            LevelScene levelScene = (LevelScene)scene;
            this.player = levelScene.player;
        }
    }

    @Override
    public void update(double dTime) {
        if(player != null) {
            if(CircleBounds.checkCollision(bounds, player.getComp(Bounds.class)) && !this.wasCollected) {
                this.wasCollected = true;
            }
        }
    }

    /**
     * Serializes the portal data
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
     * Deserializes the coin
     * @return a new portal object with the deserialized properties
     */
    public static Coin deserialize() {
        boolean wasCollected = Parser.consumeBooleanProperty("wasCollected");
        Parser.consumeEndObjectProperty();

        return new Coin(wasCollected);
    }

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
     * Abstract method to force every derived class to implement it
     *
     * @return a new object = copy of a Component
     */
    @Override
    public Component copy() {
        return new Coin(this.wasCollected, this.player);
    }
}
