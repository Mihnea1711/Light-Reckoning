package com.Game;

import com.Components.*;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.File.Parser;
import com.UserInterface.MainContainer;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LevelEditorScene extends Scene{
    public GameObject player;
    public GameObject mouseCursor;

    private GameObject ground;
    private Grid grid;
    private CameraControls cameraControls;
    private MainContainer editingButtons;

    public LevelEditorScene(String name){
        super.Scene(name);  //calls the superclass(Scene) constructor
    }

    @Override
    public void init() {
        initAssetPool();
        editingButtons = new MainContainer();
        grid = new Grid();
        cameraControls = new CameraControls();
        editingButtons.start();

        mouseCursor = new GameObject("Mouse Cursor", new Transform(new TwoPair()));
        mouseCursor.addComponent(new SnapToGrid(Constants.TileWidth, Constants.TileWidth));

        player = new GameObject("game obj", new Transform(new TwoPair(300.0f, 400.0f)));
        SpriteSheet layer1 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerOne.png");
        SpriteSheet layer2 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerTwo.png");
        SpriteSheet layer3 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerThree.png");
        Player playerComps = new Player(layer1.sprites.get(0), layer2.sprites.get(0), layer3.sprites.get(0), Color.RED, Color.GREEN);
        player.addComponent(playerComps);

        ground = new GameObject("Ground", new Transform(new TwoPair(0, Constants.GroundY)));
        ground.addComponent(new Ground());

        ground.setNonserializable();
        player.setNonserializable();
        addGameObject(player);
        addGameObject(ground);
    }

    public void initAssetPool() {
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerOne.png", 42, 42, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerTwo.png", 42, 42, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerThree.png", 42, 42, 2, 13, 13*5);

        AssetPool.addSpritesheet("Assets/Blocks.png", 42, 42, 2, 6, 12);
        AssetPool.addSpritesheet("Assets/buttonSprites.png", 60, 60, 2, 2, 2);
    }

    //call different methods and attributes on the component
    @Override
    public void update(double dTime) {
        if(camera.getPosY() > Constants.CameraOffsetGroundY) {              //if the camera off to ground is > 150, just stop it there
            camera.pos.y = Constants.CameraOffsetGroundY;
        }
        for(GameObject g : gameObjectList) {        //update every game object
            g.update(dTime);
        }
        cameraControls.update(dTime);
        grid.update(dTime);
        editingButtons.update(dTime);
        mouseCursor.update(dTime);

        if(Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_F1)) {
            export("Test");
        } else if(Window.getWindow().keyListener.isKeyPressed((KeyEvent.VK_F2))) {
            importLvl("Test");
        } else if(Window.getWindow().keyListener.isKeyPressed((KeyEvent.VK_F3))) {
            Window.getWindow().changeScene(1);
        }
    }

    private void importLvl(String filename) {
        Parser.openFile(filename);

        GameObject obj = Parser.parseGameObject();
        while(obj != null) {
            addGameObject(obj);
            obj = Parser.parseGameObject();
        }
    }

    private void export(String filename) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("levels/" + filename + ".zip");    //zipping it up to save more space
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

            zipOutputStream.putNextEntry(new ZipEntry(filename + ".json")); //putting a file inside the zip

            int i = 0;
            for(GameObject obj : gameObjectList) {
                String str = obj.serialize(0);      //0 is the tab size
                if(str.compareTo("") != 0) {        //empty string is the flag for a game object we don't want to serialize
                    zipOutputStream.write(str.getBytes());      //writing in zip files
                    if(i != gameObjectList.size() - 1) {
                        zipOutputStream.write(",\n".getBytes());
                    }
                }
                i++;
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileOutputStream.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor((Color.WHITE));
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        renderer.render(g2);
        grid.draw(g2);
        editingButtons.draw(g2);        //important before the mouse cursor
        mouseCursor.draw(g2);           //should be drawn last
    }
}
