package com.Components;

import com.Game.Component;

import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

public class TextField extends Component {
    private JTextField textField;
    float x, y, width, height;

    public TextField() {
        textField = new JTextField();
    }

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

    public String getText() {
        return textField.getText();
    }

    public JTextField getTextField() {
        return textField;
    }

    /**
     * The method which all the components extending this class will have to override.
     * @param tabSize number of tabs to be indented correctly
     * @return the string to be written into the file
     */
    @Override
    public String serialize(int tabSize) {
        return null;
    }

    /**
     * Abstract method to force every derived class to implement it
     * @return a new object = copy of a Component
     */
    @Override
    public Component copy() {
        return null;
    }
}
