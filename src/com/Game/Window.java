package com.Game;

import com.Components.Music;
import com.Components.ProgressBar;
import com.Components.TextField;
import com.Utilities.Constants;
import com.Utilities.Time;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Implements runnable because it is easier to stop/start a thread than pausing an execution.
 * Should be the only singleton = Only class with a single instance running.
 */
public class Window extends JFrame implements Runnable {
    private static Window window = null;        //the window
    public final boolean isRunning = true;           //game is running or not
    public boolean isInEditor = true;   //flag whether we are in editor or not

    public MouseListener mouseListener;
    public KeyListener keyListener;

    private Scene currentScene = null;

    //double buffering with graphics is used to show an img or frame while
    //another img or frame is being buffered to be shown next
    private Image doubleBufferImg = null;           //img used to draw things onto and then draw this inside the window
    private Graphics doubleBufferGraphics  = null;      //graphics handler for img

    private ProgressBar progressBar;
    private TextField textField;

    /**
     * Constructor that sets up the window properties.
     */
    public Window(){
        this.mouseListener = new MouseListener();
        this.keyListener = new KeyListener();

        this.setSize(Constants.ScreenWidth, Constants.ScreenHeight);        //screen size
        this.setTitle(Constants.Title);         //screen title
        this.setResizable(false);                   //no resize
        this.setVisible(true);                      //shows the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //closes window when exit
        this.setLocationRelativeTo(null);                       //centred on screen

        this.addMouseListener(mouseListener);           //clicking and dragging
        this.addMouseMotionListener(mouseListener);     //movement of mouse

        this.addKeyListener(keyListener);           //keys

        this.progressBar = new ProgressBar();
        //this.textField = new TextField(575, 250, 300, 50);
    }

    /**
     * Initializes variables inside of window class.
     * Tells which scene to start with.
     */
    public void init(){
        changeScene(2, 0,"", "", "", "", "", false);
    }

    /**
     * Helper method to get the current scene.
     * @return current scene
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Will tell which scene we need to change to.
     * @param scene index of the Scene we want to change to
     */
    public void changeScene(int scene, int prevSceneNumber, String filename, String zipFilePath,
                            String musicFile, String backgroundPath, String groundPath, boolean importLvl) {
        switch (scene) {
            case 0 -> {
                isInEditor = true;
                currentScene = new LevelEditorScene("Level Editor Scene");        //switches the scene to level editor scene
                currentScene.init(filename, "levels/CreatedLevels.zip", "", "", "", importLvl);
            }
            case 1 -> {
                isInEditor = false;
                currentScene = new LevelScene(filename);
                currentScene.init(filename, zipFilePath, musicFile, backgroundPath, groundPath, false);
            }
            case 2 -> {
                isInEditor = true;
                currentScene = new MainMenuScene("Main Menu");
                currentScene.init();
            }
            case 3 -> {
                isInEditor = true;
                currentScene = new Level1Menu("Level1Menu");
                currentScene.init();
            }
            case 4 -> {
                isInEditor = true;
                currentScene = new Level2Menu("Level2Menu");
                currentScene.init();
            }
            case 5 -> {
                isInEditor = true;
                currentScene = new Level3Menu("Level3Menu");
                currentScene.init();
            }
            case 6 -> {
                isInEditor = true;
                currentScene = new Level4Menu("Level4Menu");
                currentScene.init();
            }
            case 7 -> {
                isInEditor = true;
                currentScene = new CreateNewLevelMenu("NewLevelMenu");
                currentScene.init("", "", "", "", "", importLvl);
            }
            case 8 -> {
                isInEditor = true;
                currentScene = new OptionSelectMenu("Create/Import Lvl");
                currentScene.init();
            }
            case 9 -> {
                isInEditor = true;
                currentScene = new InfoScene(filename);
                currentScene.init(prevSceneNumber);
            }
            default -> {
                System.out.println("Don't know the scene");
                currentScene = null;
            }
        }
    }

    /**
     * Creates a window if not created already.
     * @return the window
     */
    public static Window getWindow(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    /**
     * Helper method.
     * @return the current scene
     */
    public static Scene getScene() {
        return getWindow().getCurrentScene();
    }

    /**
     * Helper method.
     * @return the mouse listener
     */
    public static MouseListener mouseListener() {
        return getWindow().mouseListener;
    }

    /**
     * Helper method.
     * @return the key listener
     */
    public static KeyListener keyListener() {
        return getWindow().keyListener;
    }

    /**
     * Helper method.
     * @return the camera x position
     */
    public static float getWindowCamX() {
        return Window.getScene().camera.getPosX();
    }

    /**
     * Helper method.
     * @return the camera y position
     */
    public static float getWindowCamY() {
        return Window.getScene().camera.getPosY();
    }

    /**
     * Helper Method.
     * @return the current scene music.
     */
    public static Music getMusic(){
        return getWindow().currentScene.levelMusic;
    }

    /**
     * Helper method.
     * @param progressBar the progress bar to be added
     */
    public void addProgressBar(ProgressBar progressBar) {
        this.add(progressBar.getBar());
    }

    /**
     * Helper method.
     */
    public void addGUI() {
        Window.getWindow().add(new PanelGUI());
    }

    /**
     * Helper method.
     * @param textField the text field to be added
     */
    public void addTextField(TextField textField) {
        //TODO::why the text field not working
        this.add(textField.getTextField());
    }

    /**
     * Updates the window/game.
     * @param dTime every frame
     */
    public void update(double dTime){
        currentScene.update(dTime);        //calls the currentScene update
        draw(getGraphics());            //draws into the scene
    }

    /**
     * Used to draw on the screen.
     * @param g graphics handler
     */
    public void draw(Graphics g){
        if(doubleBufferImg == null){
            doubleBufferImg = createImage(getWidth(), getHeight());
            doubleBufferGraphics = doubleBufferImg.getGraphics();
        }
        renderOffScreen(doubleBufferGraphics);

        g.drawImage(doubleBufferImg,0, 0, getWidth(), getHeight(), null);
    }

    /**
     * Helper method to render the img offscreen and then to draw the img.
     * Going to call the draw method on the current scene.
     * @param g graphics handler
     */
    public void renderOffScreen(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        currentScene.draw(g2);
        //should draw what is being drawn in the current scene to the screen
    }

    /**
     * Called by thread.start()
     */
    @Override
    public void run() {
        double lastFrameTime = 0.0;                             //time took to render the last frame
        try{                                                    //in case we need to thread.sleep()
            while(isRunning){                                   //while game is Running
                double time = Time.getTime();                   //start of the frame
                double dTime = time - lastFrameTime;        //current time - last frame time started
                lastFrameTime = time;

                update(dTime);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
