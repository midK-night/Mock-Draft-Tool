package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainButton extends JButton implements ActionListener {
    mainButton(String text, int x, int y) {
        this.setText(text);
        this.setLocation(x, y);
        this.setSize(200, 150);
        this.setFocusable(false);
        this.setPreferredSize(new Dimension(200, 150));
        this.setHorizontalTextPosition(JButton.CENTER);
        this.setVerticalTextPosition(JButton.CENTER);
        this.setFont(new Font("Comic Sans", Font.BOLD, 25));
        this.setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
