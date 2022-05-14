package com.Components;

import com.Game.Component;

import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

/**
 * Class for the text field inside the option select menu (new level or play created).
 */
public class TextField extends Component {
    /**
     * the actual text field
     */
    private JTextField textField;

    /**
     * Constructor.
     * @param x start x
     * @param y start y
     * @param width width
     * @param height height
     */
    public TextField(int x, int y, int width, int height) {
        textField = new JTextField();
        textField.setBounds(x, y, width, height);
        textField.setFont(new Font("Consolas", Font.PLAIN, 35));
        textField.setForeground(Color.white);
        textField.setBackground(new Color(50f / 255.0f, 10f / 255.0f, 243f / 255.0f, 1.0f));
        textField.setCaretColor(Color.white);
        textField.setMargin(new Insets(5, 10, 5, 10));
        textField.setToolTipText("Enter the level name");
    }

    /**
     * Utility method.
     * @return the text inside the text field
     */
    public String getText() {
        return textField.getText();
    }

    /**
     * Utility method.
     * @return the text field
     */
    public JTextField getTextField() {
        return textField;
    }

    /**
     * No need to implement it.
     * @param tabSize number of tabs to be indented correctly
     * @return the string to be written into the file
     */
    @Override
    public String serialize(int tabSize) {
        return null;
    }

    /**
     * No need to implement it.
     * @return a new object = copy of a Component
     */
    @Override
    public Component copy() {
        return null;
    }
}
