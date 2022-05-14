package com.Game;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class that stores all the methods needed for a completely working database.
 * Global behaviour.
 */
public class DataBaseHandler {
    public static String filename;

    /**
     * Function to establish the connection ith the database.
     * @return the connection created
     * @throws ClassNotFoundException class exception
     * @throws SQLException driver manager exception
     */
    public static Connection establishConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:LightReckoning.db");
        System.out.println("Opened Connection!");
        return conn;
    }

    /**
     * Function to create the table if not created already.
     * @param conn the connection to the database
     */
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

    /**
     * Function to update the table with the levels.
     * @param conn
     */
    public static void updateTable(Connection conn) {
        if(conn == null) return;
        try {
            //check if table was already created
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

    /**
     * Function to get the jumps inside the database.
     * @param conn connection to the database
     * @param levelName name of the level
     * @return number of jumps for the level
     */
    public static int getJumps(Connection conn, String levelName) {
        if(!(Objects.equals(levelName, "Level1") || Objects.equals(levelName, "Level2") ||
                Objects.equals(levelName, "Level3") || Objects.equals(levelName, "Level4"))) {
            return 0;
        }
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

    /**
     * Function to get the attempts inside the database.
     * @param conn connection to the database
     * @param levelName name of the level
     * @return number of attempts for the level
     */
    public static int getAttempts(Connection conn, String levelName) {
        if(!(Objects.equals(levelName, "Level1") || Objects.equals(levelName, "Level2") ||
                Objects.equals(levelName, "Level3") || Objects.equals(levelName, "Level4"))) {
            return 0;
        }
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

    /**
     * Function to get the max progression inside the database.
     * @param conn connection to the database
     * @param levelName name of the level
     * @return number of max progression for the level
     */
    public static int getCounter(Connection conn, String levelName) {
        if(!(Objects.equals(levelName, "Level1") || Objects.equals(levelName, "Level2") ||
                Objects.equals(levelName, "Level3") || Objects.equals(levelName, "Level4"))) {
            return 0;
        }
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

    /**
     * Function to get the coins inside the database.
     * @param conn connection to the database
     * @param levelName name of the level
     * @return number of coins for the level
     */
    public static int getCoins(Connection conn, String levelName) {
        if(!(Objects.equals(levelName, "Level1") || Objects.equals(levelName, "Level2") ||
                Objects.equals(levelName, "Level3") || Objects.equals(levelName, "Level4"))) {
            return 0;
        }
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

    /**
     * Function to update the attempts inside the database.
     * @param conn connection to the database
     * @param levelName name of the level
     */
    public static void updateAttempts(Connection conn, String levelName) {
        if(!(Objects.equals(levelName, "Level1") || Objects.equals(levelName, "Level2") ||
                Objects.equals(levelName, "Level3") || Objects.equals(levelName, "Level4"))) {
            return;
        }
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

    /**
     * Function to update the jumps inside the database.
     * @param conn connection to the database
     * @param levelName name of the level
     */
    public static void updateJumps(Connection conn, String levelName) {
        if(!(Objects.equals(levelName, "Level1") || Objects.equals(levelName, "Level2") ||
                Objects.equals(levelName, "Level3") || Objects.equals(levelName, "Level4"))) {
            return;
        }
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

    /**
     * Function to update the counter inside the database.
     * @param conn connection to the database
     * @param levelName name of the level
     * @param counterValue value from the progress bar
     */
    public static void updateCounter(Connection conn, String levelName, int counterValue) {
        if(!(Objects.equals(levelName, "Level1") || Objects.equals(levelName, "Level2") ||
                Objects.equals(levelName, "Level3") || Objects.equals(levelName, "Level4"))) {
            return;
        }
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

    /**
     * Function to update the coins inside the database.
     * @param conn connection to the database
     * @param levelName name of the level
     * @param collectedCoins number of coins collected
     */
    public static void updateCoins(Connection conn, String levelName, int collectedCoins) {
        if(!(Objects.equals(levelName, "Level1") || Objects.equals(levelName, "Level2") ||
                Objects.equals(levelName, "Level3") || Objects.equals(levelName, "Level4"))) {
            return;
        }
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

    /**
     * Function to set the completion inside the database.
     * When we finish a level, small bug that shows 99% completion. This gets rid of it.
     * @param conn connection to the database
     * @param levelName name of the level
     */
    public static void setCompletion(Connection conn, String levelName) {
        if(!(Objects.equals(levelName, "Level1") || Objects.equals(levelName, "Level2") ||
                Objects.equals(levelName, "Level3") || Objects.equals(levelName, "Level4"))) {
            return;
        }
        String sql = "UPDATE LevelsStats SET MaxCompletion = 100 WHERE LevelName = \"" + levelName + "\";";
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not update the completion");
        }
    }

    /**
     * Function to insert a record inside the database.
     * @param conn connection to the database
     * @param levelName name of the level
     */
    public static void insertRecord(Connection conn, String levelName) {
        if(!(Objects.equals(levelName, "Level1") || Objects.equals(levelName, "Level2") ||
                Objects.equals(levelName, "Level3") || Objects.equals(levelName, "Level4"))) {
            return;
        }
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

    /**
     * Function to get the jumps for all levels inside the database.
     * @param conn connection to the database
     * @return all jumps for all levels
     */
    public static int getAllJumps(Connection conn) {
        int jumps = 0;
        String sql = "SELECT * FROM LevelsStats";
        Statement stmt;

        try {
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while(resultSet.next()) {
                jumps += resultSet.getInt("TotalJumps");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jumps;
    }

    /**
     * Function to get the attempts for all levels inside the database.
     * @param conn connection to the database
     * @return all attempts for all levels
     */
    public static int getAllAttempts(Connection conn) {
        int attempts = 0;
        String sql = "SELECT * FROM LevelsStats";
        Statement stmt;

        try {
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while(resultSet.next()) {
                attempts += resultSet.getInt("AttemptsNr");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attempts;
    }

    /**
     * Function to get the coins for all levels inside the database.
     * @param conn connection to the database
     * @return all coins for all levels
     */
    public static int getAllCoins(Connection conn) {
        int coins = 0;
        String sql = "SELECT * FROM LevelsStats";
        Statement stmt;

        try {
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while(resultSet.next()) {
                coins += resultSet.getInt("CollectedCoins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coins;
    }

    /**
     * Function to get the completed levels inside the database.
     * @param conn connection to the database
     * @return all completed levels
     */
    public static int getAllCompletedLevels(Connection conn) {
        int completedLevels = 0;
        String sql = "SELECT * FROM LevelsStats";
        Statement stmt;

        try {
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while(resultSet.next()) {
                if (resultSet.getInt("MaxCompletion") == 100) {
                    completedLevels++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return completedLevels;
    }
}
