package Utilities;

//get the current time
public class Time {
    public static double timeStarted = System.nanoTime();

    public static double getTime(){
        return (System.nanoTime() - timeStarted) * 1E-9;    // * 10^-9 to get the result in seconds
    }
}
