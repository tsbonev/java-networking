package com.clouway.clientserver;

import javax.swing.*;

public class FramePackage {

    private JFrame frame;
    private JTextArea text;

    public FramePackage(JFrame frame, JTextArea text) {
        this.frame = frame;
        this.text = text;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JTextArea getText() {
        return text;
    }

    public void setText(JTextArea text) {
        this.text = text;
    }
}
