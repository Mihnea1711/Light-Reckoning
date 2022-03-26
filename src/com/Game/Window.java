package com.Game;

import com.Components.Music;
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
    private boolean isRunning = true;           //game is running or not
    public boolean isInEditor = true;   //flag whether we are in editor or not

    public MouseListener mouseListener;
    public KeyListener keyListener;

    private Scene currentScene = null;

    //double buffering with graphics is used to show an img or frame while
    //another img or frame is being buffered to be shown next
    private Image doubleBufferImg = null;           //img used to draw things onto and then draw this inside the window
    private Graphics doubleBufferGraphics  = null;      //graphics handler for img

    private Music stereoMadness = null;

    /**
     * Constructor that sets the window properties
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
        this.addMouseMotionListener(mouseListener);     //movement

        this.addKeyListener(keyListener);           //keys
    }

    /**
     * Initializes variables inside of window class.
     * Tells which scene to start with.
     */
    public void init(){
        changeScene(0);     //changes scene to 0 = level editor scene, 1 = levelScene
    }

    /**
     * Helper method to get the current scene
     * @return current scene
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Will tell which scene we need to change to
     * @param scene index of the Scene we want to change to
     */
    public void changeScene(int scene){
        switch(scene){
            case 0:
                isInEditor = true;
                currentScene = new LevelEditorScene("Level Editor");        //switches the scene to level editor scene
                currentScene.init();
                if(stereoMadness != null) {
                    stereoMadness.stop();
                }
                break;
            case 1:
                isInEditor = false;
                currentScene = new LevelScene("Level 1");
                currentScene.init();
                if(stereoMadness == null) {
                    stereoMadness = new Music("Assets/LevelSoundTracks/Deadlocked.wav");
                } else {
                    stereoMadness.restartClip();
                }
                break;
            case 2:
                isInEditor = true;
                currentScene = new MainMenuScene("Main Menu");
                currentScene.init();
                break;
            default:
                System.out.println("Don't know the scene");
                currentScene = null;
                break;
        }
    }

    /**
     * Creates a window if not created already
     * @return the window
     */
    public static Window getWindow(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    /**
     * Helper method
     * @return the current scene
     */
    public static Scene getScene() {
        return getWindow().getCurrentScene();
    }

    /**
     * Helper method
     * @return the mouse listener
     */
    public static MouseListener mouseListener() {
        return getWindow().mouseListener;
    }

    /**
     * Helper method
     * @return the key listener
     */
    public static KeyListener keyListener() {
        return getWindow().keyListener;
    }

    /**
     * Helper method
     * @return the camera x position
     */
    public static float getWindowCamX() {
        return Window.getScene().camera.getPosX();
    }

    /**
     * Helper method
     * @return the camera y position
     */
    public static float getWindowCamY() {
        return Window.getScene().camera.getPosY();
    }

    /**
     * Updates the window/game
     * @param dTime every frame
     */
    public void update(double dTime){
        currentScene.update(dTime);        //calls the currentScene update
        draw(getGraphics());            //draws into the scene
    }

    /**
     * Used to draw onto the screen
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
