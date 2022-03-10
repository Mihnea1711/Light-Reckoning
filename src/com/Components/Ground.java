package com.Components;

import com.Game.*;
import com.Utilities.Constants;

import java.awt.Graphics2D;
import java.awt.Color;


public class Ground extends Component {

    @Override
    public void update(double dTime) {
        if(!Window.getWindow().isInEditor) {
            LevelScene scene = (LevelScene)Window.getWindow().getCurrentScene();
            GameObject player = scene.player;

            if(player.getPosY() + player.getComp(BoxBounds.class).height > gameObject.getPosY()) {
                player.transform.pos.y = gameObject.getPosY() - player.getComp(BoxBounds.class).height;
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
        g2.drawRect((int)gameObject.getPosX(), (int)gameObject.getPosY(), Constants.ScreenWidth, Constants.ScreenHeight);

    }
}
