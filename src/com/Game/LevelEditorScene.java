package com.Game;

import com.Buttons.SceneChangerButton;
import com.Components.*;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.File.Parser;
import com.UserInterface.MainContainer;
import com.Utilities.Constants;
import com.Utilities.Pair;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This is the Editor Scene where we can create levels
 */
public class LevelEditorScene extends Scene{
    public GameObject player;
    public GameObject mouseCursor;

    private Grid grid;
    private CameraControls cameraControls;
    private MainContainer editingButtons;

    private Sprite backButton;

    private List<GameObject> buttons;

    /**
     * Calls the superclass(Scene) constructor
     * @param name name of the Scene
     */
    public LevelEditorScene(String name){
        super.Scene(name);
        this.buttons = new ArrayList<>();
    }

    /**
     * Initializes the Editor.
     */
    @Override
    public void init() {
        initAssetPool();
        editingButtons = new MainContainer();
        grid = new Grid();
        cameraControls = new CameraControls();
        editingButtons.start();

        mouseCursor = new GameObject("Mouse Cursor", new Transform(new Pair()), 10);
        mouseCursor.addComponent(new LevelEditorControls(Constants.TileWidth, Constants.TileHeight));

        player = new GameObject("game obj", new Transform(new Pair(Constants.PlayerEditorX, Constants.PlayerEditorY)), 0);
        SpriteSheet layer1 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerOne.png");
        SpriteSheet layer2 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerTwo.png");
        SpriteSheet layer3 = AssetPool.getSpritesheet("Assets/PlayerSprites/layerThree.png");
        Player playerComps = new Player(layer1.sprites.get(20), layer2.sprites.get(20), layer3.sprites.get(20), Color.RED, Color.GREEN);
        player.addComponent(playerComps);

        player.setNonserializable();
        addGameObject(player);

        initBackGrounds();

        initButtons();
    }

    /**
     * Loads all the sprites and sprite sheets that we need in the Editor.
     */
    public void initAssetPool() {
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerOne.png", Constants.PlayerWidth, Constants.PlayerHeight, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerTwo.png", Constants.PlayerWidth, Constants.PlayerHeight, 2, 13, 13*5);
        AssetPool.addSpritesheet("Assets/PlayerSprites/layerThree.png", Constants.PlayerWidth, Constants.PlayerHeight, 2, 13, 13*5);

        AssetPool.addSpritesheet("Assets/Blocks/Blocks.png", Constants.TileWidth, Constants.TileHeight, 2, 6, 12);
        AssetPool.addSpritesheet("Assets/UI/buttonSprites.png", Constants.ButtonWidth, Constants.ButtonHeight, 2, 2, 2);
        AssetPool.addSpritesheet("Assets/UI/tabs.png", Constants.TabWidth, Constants.TabHeight, 2, 6, 6);
        AssetPool.addSpritesheet("Assets/Blocks/spikes.png", Constants.TileWidth, Constants.TileHeight, 2, 6, 4);
        AssetPool.addSpritesheet("Assets/Blocks/bigBlocks.png", Constants.TileWidth * 2, Constants.TileHeight * 2, 2, 3, 3);
        AssetPool.addSpritesheet("Assets/Blocks/smallBlocks.png", Constants.TileWidth, Constants.TileHeight, 2, 6, 6);
        AssetPool.addSpritesheet("Assets/Portals/portal.png", 44, 85, 2,2, 2);
        AssetPool.addSpritesheet("Assets/Collectibles/coin.png", 75, 75, 0,1, 1);

        AssetPool.addSpritesheet("Assets/Global/back.png", 70, 74, 0,1, 1);
        this.backButton = AssetPool.getSprite("Assets/Global/back.png");
    }

    public void initButtons() {
        GameObject BackButton = new GameObject("Back", new Transform(new Pair(1150, 50)), 10);
        SceneChangerButton back = new SceneChangerButton(70, 74, backButton, backButton, 2);
        BackButton.addComponent(back);
        BackButton.setUI(true);
        BackButton.setNonserializable();
        buttons.add(BackButton);
        addGameObject(BackButton);
    }

    public void initBackGrounds() {
        GameObject ground = new GameObject("Ground", new Transform(new Pair(0, Constants.GroundY)), 1);
        ground.addComponent(new Ground());
        ground.setNonserializable();
        addGameObject(ground);

        int numBackGrounds = 5;
        GameObject[] backgrounds = new GameObject[numBackGrounds];
        GameObject[] groundBgs = new GameObject[numBackGrounds];

        for(int i = 0; i < numBackGrounds; i++) {
            ParallaxBG bg = new ParallaxBG("Assets/BackGround/bg05.png", null, ground.getComp(Ground.class), false);
            int x = i * bg.sprite.width;
            int y = 0;

            GameObject obj = new GameObject("BackGround", new Transform(new Pair(x, y)), -10);
            obj.setUI(true);
            obj.addComponent(bg);
            obj.setNonserializable();
            backgrounds[i] = obj;

            ParallaxBG groundBg = new ParallaxBG("Assets/Ground/ground01.png", null, ground.getComp(Ground.class), true);
            x = i * groundBg.sprite.width;
            y = (int) ground.transform.pos.y;
            groundBg.setGroundColor(Constants.GroundColor);

            GameObject groundObj = new GameObject("GroundBG", new Transform((new Pair(x, y))), -9);
            groundObj.addComponent(groundBg);
            groundObj.setUI(true);
            groundObj.setNonserializable();
            groundBgs[i] = groundObj;

            addGameObject(obj);
            addGameObject(groundObj);
        }
    }

    /**
     * Calls different methods and attributes on the component. Updates the Editor scene.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if(camera.getPosY() > Constants.CameraOffsetGroundY + 35) {              //to move past the bottom of the ground
            camera.pos.y = Constants.CameraOffsetGroundY + 36;  //fixes the dragging below ground bug
        }
        for(GameObject g : gameObjectList) {        //update every game object
            g.update(dTime);
        }
        for (GameObject obj : buttons) {
            obj.update(dTime);
        }
        cameraControls.update(dTime);
        grid.update(dTime);
        editingButtons.update(dTime);
        mouseCursor.update(dTime);

        //F1 - exporting the level
        //F2 - importing the level
        //F3 - plays the level
        if(Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_F1)) {
            export("Test");
        } else if(Window.getWindow().keyListener.isKeyPressed((KeyEvent.VK_F2))) {
            importLvl("Test");
        } else if(Window.getWindow().keyListener.isKeyPressed((KeyEvent.VK_F3))) {
            Window.getWindow().changeScene(1);
        }

        if(objsToRemove.size() > 0) {
            for (GameObject obj : objsToRemove) {
                gameObjectList.remove(obj);
                renderer.gameObjectList.get(obj.zIndex).remove(obj);
            }
            objsToRemove.clear();
        }
    }

    /**
     * Imports the level created with the "filename" name
     * @param filename the file from where we are importing the level
     */
    @Override
    protected void importLvl(String filename) {
        //removes the objects from the scene first
        for(GameObject obj : gameObjectList) {
            if(obj.isSerializable) {
                objsToRemove.add(obj);
            }
        }
        //removes the objects from the renderer
        for(GameObject obj : objsToRemove) {
            renderer.gameObjectList.remove(obj.zIndex, obj);
            gameObjectList.remove(obj);
        }

        //reads from the file
        Parser.openFile(filename);
        GameObject obj = Parser.parseGameObject();
        while(obj != null) {
            addGameObject(obj);
            obj = Parser.parseGameObject();
        }
    }

    /**
     * Exports the level to the zipped file created.
     * Zipping it up to save more space.
     * @param filename name of the file to create when exporting the level.
     */
    private void export(String filename) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("levels/" + filename + ".zip");    //zipping it up to save more space
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);    //creating a zip file in java

            zipOutputStream.putNextEntry(new ZipEntry(filename + ".json")); //putting a file.json inside the zip

            //writing to the file
            int i = 0;
            for(GameObject obj : gameObjectList) {
                //serialize all the game objects in the level
                String str = obj.serialize(0);      //0 is the tab size
                if(str.compareTo("") != 0) {        //empty string is the flag for a game object we don't want to serialize
                    zipOutputStream.write(str.getBytes());      //writing in zip files
                    if(i != gameObjectList.size() - 1) {
                        zipOutputStream.write(",\n".getBytes());    //write a comma to separate all the game objects
                    }
                }
                i++;
            }
            zipOutputStream.closeEntry();       //close the entry for the zip file
            zipOutputStream.close();            //close the zip file output stream
            fileOutputStream.close();           //close the file output stream
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Draws into the level editor scene
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Constants.BgColor);
        g2.fillRect(0, 0, Constants.ScreenWidth, Constants.ScreenHeight);

        renderer.render(g2);
        grid.draw(g2);
        editingButtons.draw(g2);
        mouseCursor.draw(g2);           //should be drawn last, on top of everything
    }
}
