package com.main;

import com.Game.DataBaseHandler;
import com.Game.Window;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static Connection conn;

    /**
     * Entry point for the program
     * @param args main function parameter
     */
    public static void main(String[] args){
        try {
            conn = DataBaseHandler.establishConnection();

            DataBaseHandler.updateTable(conn != null ? conn : null);

            Window window = Window.getWindow();
            window.init();      //initialization function

            Thread mainThread = new Thread(window);
            mainThread.start();     //calls the run method

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if(conn != null && !Window.getWindow().isRunning) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}




//TODO:: add scene for pause/ restart/ music on/off / progress-bar on/off
//TODO:: add scene for level ending and stats
//TODO:: add multiple levels
//TODO:: keep the stats in a created database
