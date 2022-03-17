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

        mouseCursor = new GameObject("Mouse Cursor", new Transform(new TwoPair()), 10);
        mouseCursor.addComponent(new SnapToGrid(Constants.TileWidth, Constants.TileWidth));

        player = new GameObject("game obj", new Transform(new TwoPair(300.0f, 400.0f)), 0);
        SpriteSheet layer1 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerOne.png");
        SpriteSheet layer2 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerTwo.png");
        SpriteSheet layer3 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerThree.png");
        Player playerComps = new Player(layer1.sprites.get(0), layer2.sprites.get(0), layer3.sprites.get(0), Color.RED, Color.GREEN);
        player.addComponent(playerComps);

        player.setNonserializable();
        addGameObject(player);

        initBackGrounds();
    }

    public void initAssetPool() {
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerOne.png", Constants.PlayerWidth, Constants.PlayerHeight, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerTwo.png", Constants.PlayerWidth, Constants.PlayerHeight, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerThree.png", Constants.PlayerWidth, Constants.PlayerHeight, 2, 13, 13*5);

        AssetPool.addSpritesheet("Assets/Blocks.png", Constants.TileWidth, Constants.TileHeight, 2, 6, 12);
        AssetPool.addSpritesheet("Assets/UI/buttonSprites.png", 60, 60, 2, 2, 2);
        AssetPool.addSpritesheet("Assets/UI/tabs.png", Constants.TabWidth, Constants.TabHeight, 2, 6, 6);
        AssetPool.addSpritesheet("Assets/spikes.png", Constants.TileWidth, Constants.TileHeight, 2, 6, 4);
        AssetPool.addSpritesheet("Assets/bigSprites.png", 84, 84, 2, 2, 2);
        AssetPool.addSpritesheet("Assets/smallBlocks.png", Constants.TileWidth, Constants.TileHeight, 2, 6, 1);
        AssetPool.addSpritesheet("Assets/portal.png", 44, 85, 2,2, 2);
    }

    public void initBackGrounds() {
        ground = new GameObject("Ground", new Transform(new TwoPair(0, Constants.GroundY)), 1);
        ground.addComponent(new Ground());
        ground.setNonserializable();
        addGameObject(ground);

        int numBackGrounds = 5;
        GameObject[] backgrounds = new GameObject[numBackGrounds];
        GameObject[] groundBgs = new GameObject[numBackGrounds];

        for(int i = 0; i < numBackGrounds; i++) {
            ParallaxBG bg = new ParallaxBG("Assets/BackGround/bg01.png", null, ground.getComp(Ground.class), false);
            int x = i * bg.sprite.width;
            int y = 0;

            GameObject obj = new GameObject("BackGround", new Transform(new TwoPair(x, y)), -10);
            obj.setUI(true);
            obj.addComponent(bg);
            obj.setNonserializable();
            backgrounds[i] = obj;

            ParallaxBG groundBg = new ParallaxBG("Assets/Ground/ground01.png", null, ground.getComp(Ground.class), true);
            x = i * groundBg.sprite.width;
            y = (int)ground.transform.pos.y;

            GameObject groundObj = new GameObject("GroundBG", new Transform((new TwoPair(x, y))), -9);
            groundObj.addComponent(groundBg);
            groundObj.setUI(true);
            groundObj.setNonserializable();
            groundBgs[i] = groundObj;

            addGameObject(obj);
            addGameObject(groundObj);
        }
    }

    //call different methods and attributes on the component
    @Override
    public void update(double dTime) {
        if(camera.getPosY() > Constants.CameraOffsetGroundY + 35) {              //if the camera off to ground is > 150, just stop it there
            camera.pos.y = Constants.CameraOffsetGroundY + 36;
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
        g2.setColor(Constants.BgColor);
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        renderer.render(g2);
        grid.draw(g2);
        editingButtons.draw(g2);        //important before the mouse cursor
        mouseCursor.draw(g2);           //should be drawn last
    }
}
