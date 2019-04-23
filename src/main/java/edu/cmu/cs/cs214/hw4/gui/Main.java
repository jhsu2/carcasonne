package edu.cmu.cs.cs214.hw4.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.*;
import java.io.IOException;

/**
 * main class to run the gui
 */
public class Main {
    /**
     * main method
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //add frame and set its closing operation
            JFrame frame = new JFrame("Carcass0nne");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            //add game client
            frame.add(new CarcassonneClientInit(frame));

            //display the JFrame
            frame.pack();
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}
