package com.Components;

import com.Game.Component;
import com.Game.GameObject;
import com.Game.LevelScene;
import com.Game.Window;
import com.Utilities.Constants;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Class for the ground component.
 */
public class Ground extends Component {

    /**
     * Updates the ground every frame.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if(!Window.getWindow().isInEditor) {
            LevelScene scene = (LevelScene)Window.getWindow().getCurrentScene();
            GameObject player = scene.player;

            if(player.getPosY() + player.getComp(BoxBounds.class).getHeight() > gameObject.getPosY()) {
                player.transform.pos.y = gameObject.getPosY() - player.getComp(BoxBounds.class).getHeight();

                player.getComp(Player.class).onGround = true;       //we hit the ground, make onGround true
            }
            gameObject.transform.pos.x = scene.camera.getPosX();
        } else {
            gameObject.transform.pos.x = Window.getWindowCamX();        //in loc de Window.getWindow().getCurrentScene().camera.getPosX();
        }
    }

    /**
     * Draws the ground.
     * Constants.GroundOffsetX and GroundOffsetScreenWidth are constants to ensure that there are no bugs when dragging the screen left/right
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);

        //the ground will follow the camera and will be a rect along the screen
        g2.drawRect((int)gameObject.getPosX() + Constants.GroundOffsetX, (int)gameObject.getPosY(),
                Constants.ScreenWidth + Constants.GroundOffsetScreenWidth, Constants.ScreenHeight);

    }

    /**
     * Copy method for the ground (no need).
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
