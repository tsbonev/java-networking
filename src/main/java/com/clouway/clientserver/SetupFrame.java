package com.clouway.clientserver;

import javax.swing.*;

public class SetupFrame {

    public static FramePackage setupFrame(){

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocation(0, 0);

        JPanel panel = new JPanel();
        JTextArea text = new JTextArea(20, 20);
        text.setEditable(false);
        panel.add(text);

        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);

        return new FramePackage(frame, text);

    }

}
