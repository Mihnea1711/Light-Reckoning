package com.Components;

import com.File.Parser;
import com.Game.*;

import static com.main.Main.conn;

/**
 * Class for the end-of-the-level pipe part.
 */
public class PipePart extends Component {
    /**
     * flag for exiting the level
     */
    boolean exitOnCollision;

    /**
     * reference to the player
     */
    public GameObject player;               //reference to the player

    /**
     * reference to the bounds
     */
    private BoxBounds bounds;               //reference to the bounds

    /**
     * Constructor.
     * @param exitOnCollision flag whether we want to exit on collision or not.
     */
    public PipePart(boolean exitOnCollision) {
        this.exitOnCollision = exitOnCollision;
    }

    /**
     * Constructor.
     * @param exitOnCollision flag whether we want to exit on collision or not.
     * @param player player
     */
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
     * Updates the state of the player on collision.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if(player != null) {
            if (exitOnCollision && BoxBounds.checkCollision(bounds, player.getComp(BoxBounds.class))) {
                DataBaseHandler.updateCoins(conn, Window.getScene().name, player.getComp(Player.class).getCollectedCoins());
                DataBaseHandler.setCompletion(conn, Window.getScene().name);
                if(Window.getMusic() != null){
                    Window.getMusic().stop();
                }
                Window.getWindow().changeScene(3, 1,"", "", "", "", "", false);
            }
        }
    }

    /**
     * Serializes the Pipe data.
     * @param tabSize number of tabs to be indented correctly
     * @return the pipe properties serialized
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
     * Deserializes the Pipe data.
     * @return a new pipe object with the deserialized properties
     */
    public static PipePart deserialize() {
        boolean exit = Parser.consumeBooleanProperty("Exit");
        Parser.consumeEndObjectProperty();

        return new PipePart(exit);
    }

    /**
     * Creates a new block object with the same properties, instead of passing a reference around.
     * It doesn't necessarily invoke start.
     * @return a new pipe object
     */
    @Override
    public Component copy() {
        return new PipePart(this.exitOnCollision, this.player);
    }
}
