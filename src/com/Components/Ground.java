package com.Components;

import com.Game.Component;
import com.Game.GameObject;
import com.Game.LevelEditorScene;
import com.Utilities.Constants;

import javax.swing.*;
import java.awt.*;

public class Ground extends Component {

    @Override
    public void update(double dTime) {
        GameObject player = LevelEditorScene.getScene().player;

        if(player.getPosY() + player.getComp(BoxBounds.class).height > gameObject.getPosY()) {
            player.transform.pos.y = gameObject.getPosY() - player.getComp(BoxBounds.class).height;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        //the ground will follow the camera and will be a rect along the screen
        g2.drawRect((int)gameObject.getPosX(), (int)gameObject.getPosY(), Constants.ScreenWidth, Constants.ScreenHeight);

    }
}
