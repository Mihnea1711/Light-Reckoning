package com.Game;

import com.Components.BoxBounds;
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
        testObj = new GameObject("game obj", new Transform(new TwoPair(0.0f, 0.0f)));
        testObj.addComponent(new BoxBounds("Box"));
        testObj.addComponent(AssetPool.getSprite("Assets/img1.png"));
    }

    @Override
    public void update(double dTime) {
        //call different methods and attributes on the component
        System.out.println(testObj.getComp(BoxBounds.class).name);
        testObj.update(dTime);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor((Color.BLUE));
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        testObj.draw(g2);
    }
}
