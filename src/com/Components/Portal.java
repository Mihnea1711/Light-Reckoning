package com.Components;

import com.File.Parser;
import com.Game.*;

public class Portal extends Component {
    public PlayerState stateChanger;
    public GameObject player;
    private BoxBounds bounds;

    public Portal(PlayerState stateChanger) {
        this.stateChanger = stateChanger;
    }

    public Portal(PlayerState stateChanger, GameObject player) {
        this.stateChanger = stateChanger;
        this.player = player;
    }

    @Override
    public void start() {
        this.bounds = gameObject.getComp(BoxBounds.class);
        Scene scene = Window.getScene();
        if(scene instanceof LevelScene) {
            LevelScene levelScene = (LevelScene)scene;
            this.player = levelScene.player;
        }
    }

    @Override
    public void update(double dTime) {
        if(player != null) {
            if (player.getComp(Player.class).state != stateChanger && BoxBounds.checkCollision(bounds, player.getComp(BoxBounds.class))) {
                player.getComp(Player.class).state = stateChanger;
            }
        }
    }


    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        int state = this.stateChanger == PlayerState.Flying ? 1 : 0;

        builder.append(beginObjectProperty("Portal", tabSize));
        builder.append(addIntProperty("StateChanger", state, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    public static Portal deserialize() {
        int state = Parser.consumeIntProperty("StateChanger");
        Parser.consumeEndObjectProperty();

        PlayerState stateChanger = state == 1 ? PlayerState.Flying : PlayerState.Normal;

        return new Portal(stateChanger);
    }

    @Override
    public Component copy() {
        return new Portal(this.stateChanger, this.player);
    }
}
