package com.Utilities;

/**
 * Used to get the current time.
 */
public class Time {
    public static double timeStarted = System.nanoTime();       //when initialized this will store the time we are at

    /**
     * Utility method.
     * @return time passed since the function call
     */
    public static double getTime(){
        return (System.nanoTime() - timeStarted) * 1E-9;    // * 10^-9 to get the result in seconds
    }
}
