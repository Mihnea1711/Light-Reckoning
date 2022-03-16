package com.Components;

import com.Game.Component;
import com.Game.GameObject;
import com.Game.LevelScene;
import com.Game.Window;
import com.Utilities.Constants;

import java.awt.Color;
import java.awt.Graphics2D;


public class Ground extends Component {

    @Override
    public void update(double dTime) {
        if(!Window.getWindow().isInEditor) {
            LevelScene scene = (LevelScene)Window.getWindow().getCurrentScene();
            GameObject player = scene.player;

            if(player.getPosY() + player.getComp(BoxBounds.class).height > gameObject.getPosY()) {
                player.transform.pos.y = gameObject.getPosY() - player.getComp(BoxBounds.class).height;

                player.getComp(Player.class).onGround = true;       //we hit the ground, make onGround true
            }
            gameObject.transform.pos.x = scene.camera.getPosX();
        } else {
            gameObject.transform.pos.x = Window.getWindowCamX();        //in loc de Window.getWindow().getCurrentScene().camera.getPosX();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        //the ground will follow the camera and will be a rect along the screen
        g2.drawRect((int)gameObject.getPosX() + Constants.GroundOffsetX, (int)gameObject.getPosY(), Constants.ScreenWidth + Constants.GroundOffsetScreenWidth, Constants.ScreenHeight);

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
