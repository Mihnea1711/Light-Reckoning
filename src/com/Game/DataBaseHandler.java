package com.Game;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DataBaseHandler {
    public static String filename;

    public static Connection establishConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:LightReckoning.db");
        System.out.println("Opened Connection!");
        return conn;
    }

    public static void createTable(Connection conn) {
        String sql = "" +
                "CREATE TABLE LevelsStats " +
                "(" +
                "LevelName varchar(20) NOT NULL, " +
                "TotalJumps integer, " +
                "AttemptsNr integer, " +
                "MaxCompletion integer ," +
                "CollectedCoins integer " +
                ");";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Table creation failed");
        }
    }

    public static void updateTable(Connection conn) {
        if(conn == null) return;
        try {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet table = dbm.getTables(null, null, "LevelsStats", null);
            if(!table.next()) {
                createTable(conn);
                ZipFile levels;
                try {
                    File check = new File("levels/levels.zip");
                    if(check.exists()) {
                        levels = new ZipFile("levels/levels.zip");

                        Enumeration<? extends ZipEntry> entries = levels.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry zipEntry = entries.nextElement();
                            insertRecord(conn, zipEntry.getName().substring(0, zipEntry.getName().length() - 5));
                        }
                        levels.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getJumps(Connection conn, String levelName) {
        String sql = "SELECT * FROM LevelsStats WHERE LevelName = \"" + levelName + "\";";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            return resultSet.getInt("TotalJumps");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not get the query for the result set");
        }
        return -1;      //should not get here
    }

    public static int getAttempts(Connection conn, String levelName) {
        String sql = "SELECT * FROM LevelsStats WHERE LevelName = \"" + levelName + "\";";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            return resultSet.getInt("AttemptsNr");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not get the query for the result set");
        }
        return -1;      //should not get here
    }

    public static int getCounter(Connection conn, String levelName) {
        String sql = "SELECT * FROM LevelsStats WHERE LevelName = \"" + levelName + "\";";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            return resultSet.getInt("MaxCompletion");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not get the query for the result set");
        }
        return -1;      //should not get here
    }

    public static int getCoins(Connection conn, String levelName) {
        int result = 0;
        String sql = "SELECT * FROM LevelsStats WHERE LevelName = \"" + levelName + "\";";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            result = resultSet.getInt("CollectedCoins");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not get the query for the result set");
        }
        return result;      //should not get here
    }

    public static void updateAttempts(Connection conn, String levelName) {
        String sql = "UPDATE LevelsStats SET AttemptsNr = " + getAttempts(conn, levelName) + " + 1 " +
                "WHERE LevelName = \"" + levelName + "\";";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not update the attempts");
        }
    }

    public static void updateJumps(Connection conn, String levelName) {
        String sql = "UPDATE LevelsStats SET TotalJumps = " + getJumps(conn, levelName) + " + 1" +
                " WHERE LevelName = \"" + levelName + "\";";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not update the jumps");
        }
    }

    public static void updateCounter(Connection conn, String levelName, int counterValue) {
        String sql = "UPDATE LevelsStats SET MaxCompletion = " + Integer.max(getCounter(conn, levelName), counterValue) +
                " WHERE LevelName = \"" + levelName + "\";";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not update the jumps");
        }
    }

    public static void updateCoins(Connection conn, String levelName, int collectedCoins) {
        if(conn == null) return;
        String sql = "UPDATE LevelsStats SET CollectedCoins = " + Integer.max(getCoins(conn, levelName), collectedCoins) +
                " WHERE LevelName = \"" + levelName + "\";";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not update the coins");
        }
    }

    public static void insertRecord(Connection conn, String levelName) {
        String sql = "INSERT INTO LevelsStats VALUES(?, 0, 0, 0, 0);";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, levelName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Record could not be inserted properly");
        }
    }
}
