package com.clouway.clientserver;

import javax.swing.*;

@SuppressWarnings("Duplicates")
public class ClientServer {

    private JFrame frame;
    private JPanel panel;
    private JTextArea text;

    private Client client;
    private Server server;

    public ClientServer(Client client, Server server) {
        this.client = client;
        this.server = server;
    }

    public void start(){

        openFrame();

        client.setFrame(frame, text);
        server.setFrame(frame, text);

        Thread clientThread = new Thread(client);
        Thread serverThread = new Thread(server);

        serverThread.start();
        clientThread.start();


    }

    public void openFrame(){

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocation(0, 0);

        panel = new JPanel();
        text = new JTextArea(20, 20);
        text.setEditable(false);
        panel.add(text);

        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);

    }

}
