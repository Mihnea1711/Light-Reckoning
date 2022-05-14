package com.Components;

import com.File.Parser;
import com.Game.*;

/**
 * Class for the Portal's properties.
 */
public class Portal extends Component {
    public PlayerState stateChanger;        //functionality of the portal
    public GameObject player;               //reference to the player
    private BoxBounds bounds;               //reference to the bounds

    /**
     * Constructor for the portal.
     * @param stateChanger functionality of the portal
     */
    public Portal(PlayerState stateChanger) {
        this.stateChanger = stateChanger;
    }

    /**
     * Constructor for the portal.
     * @param stateChanger functionality of the portal
     * @param player reference to the player
     */
    public Portal(PlayerState stateChanger, GameObject player) {
        this.stateChanger = stateChanger;
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
            if (player.getComp(Player.class).state != stateChanger && BoxBounds.checkCollision(bounds, player.getComp(BoxBounds.class))) {
                player.getComp(Player.class).state = stateChanger;
                player.transform.rotation = 0;
            }
        }
    }

    /**
     * Serializes the portal data.
     * @param tabSize number of tabs to be indented correctly
     * @return the portal properties serialized
     */
    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        int state = this.stateChanger == PlayerState.Flying ? 1 : 0;        //if there will be more states, try another method

        builder.append(beginObjectProperty("Portal", tabSize));
        builder.append(addIntProperty("StateChanger", state, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    /**
     * Deserializes the portal.
     * @return a new portal object with the deserialized properties
     */
    public static Portal deserialize() {
        int state = Parser.consumeIntProperty("StateChanger");
        Parser.consumeEndObjectProperty();

        PlayerState stateChanger = state == 1 ? PlayerState.Flying : PlayerState.Normal;

        return new Portal(stateChanger);
    }

    /**
     * Creates a new portal object with the same properties, instead of passing a reference around.
     * It doesn't necessarily invoke start.
     * @return a new portal object
     */
    @Override
    public Component copy() {
        return new Portal(this.stateChanger, this.player);
    }
}
