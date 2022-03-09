package Game;

import Utilities.Constants;

import java.awt.*;

public class LevelEditorScene extends Scene{

    public LevelEditorScene(String name){
        super.Scene(name);  //calls the superclass(Scene) constructor
    }

    @Override
    public void init() {

    }

    @Override
    public void update(double dTime) {
        System.out.println("in here");
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor((Color.PINK));
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);
    }
}
