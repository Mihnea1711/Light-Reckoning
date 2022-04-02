package com.Components;

import com.Game.Component;

import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.Font;

public class ProgressBar extends Component {
    public JProgressBar bar = new JProgressBar(0, 100);
    private float counter = 0.0f;

    public ProgressBar(){
        bar.setValue(0);                                        //initial value
        bar.setBounds(415, 30, 450, 20);        //bar properties
        bar.setFont(new Font("MV Boli", Font.BOLD, 13));
        bar.setForeground(Color.RED);
        bar.setBackground(Color.BLACK);

        bar.setStringPainted(true);                             //adds percentage to progress bar
    }

    @Override
    public void update(double dTime) {
        if(counter < 100) {
            bar.setValue((int)(counter));
        } else {
            bar.setString("Completed!");
        }
    }

    public float getCounterValue() {
        return counter;
    }

    public void setCounterValue(float value) {
        counter = value;
    }

    /**
     * The method which all the components extending this class will have to override.
     *
     * @param tabSize number of tabs to be indented correctly
     * @return the string to be written into the file
     */
    @Override
    public String serialize(int tabSize) {
        return null;
    }

    /**
     * Abstract method to force every derived class to implement it
     *
     * @return a new object = copy of a Component
     */
    @Override
    public Component copy() {
        return null;
    }
}
