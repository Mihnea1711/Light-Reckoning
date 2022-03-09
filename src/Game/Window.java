package Game;

import Utilities.Time;

import javax.swing.JFrame;

//implement runnable because it is easier to stop the game
public class Window extends JFrame implements Runnable {
    private static Window window = null;
    private boolean isRunning = true;

    //Constructor
    public Window(){
        this.setSize(1280, 720);        //screen size
        this.setTitle("Light's Reckoning");         //screen title
        this.setResizable(false);                   //no resize
        this.setVisible(true);                      //shows the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //closes window when exit
        this.setLocationRelativeTo(null);                       //centred on screen
    }

    //initialize variables inside of window class
    public void init(){

    }

    //singleton class
    public static Window getWindow(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void update(double dt){
        System.out.println(dt);
    }

    //called by thread.start()
    @Override
    public void run() {
        double lastFrameTime = 0.0;                             //time took to render the last frame
        try{                                                    //in case we need to thread.sleep()
            while(isRunning){                                   //while game is Running
                double time = Time.getTime();                   //start of the frame
                double deltaTime = time - lastFrameTime;        //current time - last frame time started
                lastFrameTime = time;

                update(time);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
