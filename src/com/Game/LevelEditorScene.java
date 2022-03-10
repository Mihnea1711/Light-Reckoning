package com.Game;

import com.Components.BoxBounds;
import com.Components.Player;
import com.Components.SpriteSheet;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

import javax.swing.*;
import java.awt.*;

public class LevelEditorScene extends Scene{

    GameObject player;

    public LevelEditorScene(String name){
        super.Scene(name);  //calls the superclass(Scene) constructor
    }

    @Override
    public void init() {
        player = new GameObject("game obj", new Transform(new TwoPair(200.0f, 500.0f)));
        SpriteSheet layer1 = new SpriteSheet("Assets/PlayerSprites/layerOne.png", 42, 42, 2, 13, 13*5);
        SpriteSheet layer2 = new SpriteSheet("Assets/PlayerSprites/layerTwo.png", 42, 42, 2, 13, 13*5);
        SpriteSheet layer3 = new SpriteSheet("Assets/PlayerSprites/layerThree.png", 42, 42, 2, 13, 13*5);
        Player playerComp = new Player(layer1.sprites.get(0), layer2.sprites.get(0), layer3.sprites.get(0), Color.RED, Color.GREEN);
        player.addComponent(playerComp);

    }

    //call different methods and attributes on the component
    @Override
    public void update(double dTime) {
        player.update(dTime);
        player.transform.rotation += dTime * 5f;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor((Color.DARK_GRAY));
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        player.draw(g2);
    }
}
