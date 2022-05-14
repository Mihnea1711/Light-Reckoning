package com.Components;

import com.Game.Component;

import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.Font;

/**
 * Class for the progress bar.
 */
public class ProgressBar extends Component {
    /**
     * the actual progress bar
     */
    private JProgressBar bar = new JProgressBar(0, 100);

    /**
     * the progress bar counter
     */
    private float counter = 0.0f;

    /**
     * Constructor.
     */
    public ProgressBar(){
        bar.setValue(0);                                        //initial value
        bar.setBounds(415, 30, 450, 20);        //bar properties
        bar.setFont(new Font("MV Boli", Font.BOLD, 13));
        bar.setForeground(Color.RED);
        bar.setBackground(Color.BLACK);

        bar.setStringPainted(true);                             //adds percentage to progress bar
    }

    /**
     * Update method for the bar.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if(counter < 100) {
            bar.setValue((int)(counter));
        } else {
            bar.setString("Completed!");
        }
    }

    /**
     * Utility method.
     * @return the progress bar
     */
    public JProgressBar getBar() {
        return bar;
    }

    /**
     * Utility method.
     * @return the percentage
     */
    public float getCounterValue() {
        return counter;
    }

    /**
     * Utility method.
     * @param value the value to be stored in the bar
     */
    public void setCounterValue(float value) {
        counter = value;
    }

    /**
     * No need to implement it.
     *
     * @param tabSize number of tabs to be indented correctly
     * @return the string to be written into the file
     */
    @Override
    public String serialize(int tabSize) {
        return null;
    }

    /**
     * No need to implement it.
     *
     * @return a new object = copy of a Component
     */
    @Override
    public Component copy() {
        return null;
    }
}
