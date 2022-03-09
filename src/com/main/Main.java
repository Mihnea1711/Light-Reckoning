package com.main;

import com.Game.Window;

public class Main {
    //entry point for program
    public static void main(String[] args){
        Window window = Window.getWindow();
        window.init();//initialization function

        Thread mainThread = new Thread(window);
        mainThread.start(); //calls the run() method
    }
}
