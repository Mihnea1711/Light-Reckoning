package com.Game;

import com.Components.TextField;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelGUI extends JPanel implements ActionListener {

    private JPanel panel;

    public PanelGUI() {
        panel = new JPanel(new GridLayout(1, 2));
        panel.setSize(500, 200);
        panel.setLocation(0, 0);

        JButton button = new JButton("Submit");
        TextField textField = new TextField(475, 250, 300, 50);

        panel.add(textField.getTextField());
        panel.add(button);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
