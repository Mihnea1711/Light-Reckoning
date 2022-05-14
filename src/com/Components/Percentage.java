package com.Components;

import com.Game.Component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Class for the percentage shown in the playable level. (about progression)
 */
public class Percentage extends Component {
    /**
     * percentage text
     */
    String text;

    /**
     * Constructor.
     */
    public Percentage() {
        text = "";
    }

    /**
     * Utility method to set the text.
     * @param val the percentage to be shown
     */
    public void setText(String val) {
        text = val;
    }

    /**
     * Utility method to get the current percentage.
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * Don't need to implement it.
     *
     * @param tabSize number of tabs to be indented correctly
     * @return the string to be written into the file
     */
    @Override
    public String serialize(int tabSize) {
        return null;
    }

    /**
     * Don't need to implement it.
     *
     * @return a new object = copy of a Component
     */
    @Override
    public Component copy() {
        return null;
    }

    /**
     * Draw method.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Calibri", Font.BOLD, 40));
        g2.drawString(this.getText(), 620, 100);
    }
}
