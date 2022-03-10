package com.Game;

import com.Components.BoxBounds;
import com.Components.SpriteSheet;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

import javax.swing.*;
import java.awt.*;

public class LevelEditorScene extends Scene{

    GameObject testObj;

    public LevelEditorScene(String name){
        super.Scene(name);  //calls the superclass(Scene) constructor
    }

    @Override
    public void init() {
        testObj = new GameObject("game obj", new Transform(new TwoPair(200.0f, 500.0f)));
        SpriteSheet spriteSheet = new SpriteSheet("Assets/PlayerSprites/layerOne.png", 42, 42, 2, 13, 13*5);
        testObj.addComponent(spriteSheet.sprites.get(44));
    }

    //call different methods and attributes on the component
    @Override
    public void update(double dTime) {
        testObj.update(dTime);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor((Color.BLUE));
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        testObj.draw(g2);
    }
}
