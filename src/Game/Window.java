package Game;

import Utilities.Time;

import javax.swing.JFrame;
import javax.xml.stream.XMLEventReader;
import java.awt.event.KeyEvent;

//implement runnable because it is easier to stop the game
public class Window extends JFrame implements Runnable {
    private static Window window = null;
    private boolean isRunning = true;

    public MouseListener mouseListener;
    public KeyListener keyListener;

    private Scene currentScene = null;

    //Constructor
    public Window(){
        this.mouseListener = new MouseListener();
        this.keyListener = new KeyListener();

        this.setSize(1280, 720);        //screen size
        this.setTitle("Light's Reckoning");         //screen title
        this.setResizable(false);                   //no resize
        this.setVisible(true);                      //shows the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //closes window when exit
        this.setLocationRelativeTo(null);                       //centred on screen

        this.addMouseListener(mouseListener);           //clicking and dragging
        this.addMouseMotionListener(mouseListener);     //movement

        this.addKeyListener(keyListener);
    }

    //initialize variables inside of window class
    public void init(){
        changeScene(0);
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

    public void update(double dt){
        currentScene.update(dt);
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
