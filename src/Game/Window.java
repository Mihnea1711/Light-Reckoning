package Game;

import Utilities.Constants;
import Utilities.Time;

import javax.swing.JFrame;
import javax.xml.stream.XMLEventReader;
import java.awt.*;
import java.awt.event.KeyEvent;

//implement runnable because it is easier to stop the game
public class Window extends JFrame implements Runnable {
    private static Window window = null;
    private boolean isRunning = true;

    public MouseListener mouseListener;
    public KeyListener keyListener;

    private Scene currentScene = null;

    private Image doubleBufferImg = null;           //img used to draw things onto and then draw this inside the window
    private Graphics doubleBufferGraph  = null;      //graphics handler for img

    //Constructor
    public Window(){
        this.mouseListener = new MouseListener();
        this.keyListener = new KeyListener();

        this.setSize(Constants.ScreenWidth, Constants.ScreenHeight);        //screen size
        this.setTitle(Constants.title);         //screen title
        this.setResizable(false);                   //no resize
        this.setVisible(true);                      //shows the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //closes window when exit
        this.setLocationRelativeTo(null);                       //centred on screen

        this.addMouseListener(mouseListener);           //clicking and dragging
        this.addMouseMotionListener(mouseListener);     //movement

        this.addKeyListener(keyListener);           //keys
    }

    //initialize variables inside of window class
    public void init(){
        changeScene(0);     //changes scene to 0 = level editor scene
    }

    //will tell which scene we need to change to
    public void changeScene(int scene){
        switch(scene){
            case 0:
                currentScene = new LevelEditorScene("Level Editor");        //switches the scene to level editor scene
                break;
            default:
                System.out.println("Don't know the scene");
                currentScene = null;
                break;
        }
    }

    //singleton class
    public static Window getWindow(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    //updates the window
    public void update(double dTime){
        currentScene.update(dTime);        //calls the currentScene update
        draw(getGraphics());            //draws into the scene
    }

    //used to draw
    public void draw(Graphics g){
        if(doubleBufferImg == null){
            doubleBufferImg = createImage(getWidth(), getHeight());
            doubleBufferGraph = doubleBufferImg.getGraphics();
        }
        renderOffScreen(doubleBufferGraph);

        g.drawImage(doubleBufferImg,0, 0, getWidth(), getHeight(), null);
    }

    //helper method to render the img off screen and then to draw the img
    public void renderOffScreen(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        currentScene.draw(g2);
        //should draw what is being draw in the current scene
    }

    //called by thread.start()
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
