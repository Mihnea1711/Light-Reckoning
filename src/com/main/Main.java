package com.main;

import com.Game.Window;

public class Main {
    /**
     * Entry point for the program
     * @param args main function parameter
     */
    public static void main(String[] args){
        Window window = Window.getWindow();
        window.init();      //initialization function

        Thread mainThread = new Thread(window);
        mainThread.start(); //calls the run() method
    }
}
