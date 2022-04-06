package com.Components;

import com.File.Parser;
import com.Game.*;

public class PipePart extends Component {
    boolean exitOnCollision;
    public GameObject player;               //reference to the player
    private BoxBounds bounds;               //reference to the bounds

    public PipePart(boolean exitOnCollision) {
        this.exitOnCollision = exitOnCollision;
    }

    public PipePart(boolean exitOnCollision, GameObject player) {
        this.exitOnCollision = exitOnCollision;
        this.player = player;
    }

    /**
     * Sets the bounds and the player after the object is created.
     */
    @Override
    public void start() {
        this.bounds = gameObject.getComp(BoxBounds.class);
        Scene scene = Window.getScene();
        if(scene instanceof LevelScene) {       //checks if it is a level scene. will tell if we are in level scene or in editor
            LevelScene levelScene = (LevelScene)scene;
            this.player = levelScene.player;
        }
    }

    /**
     * Updates the state of the player on collision
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if(player != null) {
            if (exitOnCollision && BoxBounds.checkCollision(bounds, player.getComp(BoxBounds.class))) {
                System.out.println("Total jumps: " + player.getComp(Player.class).getJumps());
                System.out.println("Coins collected: " + player.getComp(Player.class).getCollectedCoins());
                Window.getWindow().changeScene(3, "", "", "", "");
            }
        }
    }

    /**
     * Serializes the Pipe data
     * @param tabSize   number of tabs to be indented correctly
     * @return the portal properties serialized
     */
    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("MarioPipe", tabSize));
        builder.append(addBooleanProperty("Exit", exitOnCollision, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    /**
     * Deserializes the portal
     * @return a new portal object with the deserialized properties
     */
    public static PipePart deserialize() {
        boolean exit = Parser.consumeBooleanProperty("Exit");
        Parser.consumeEndObjectProperty();

        return new PipePart(exit);
    }

    /**
     * Creates a new portal object with the same properties, instead of passing a reference around.
     * It doesn't necessarily invoke start.
     * @return a new portal object
     */
    @Override
    public Component copy() {
        return new PipePart(this.exitOnCollision, this.player);
    }
}
