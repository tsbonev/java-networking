package com.clouway.clientserver;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;

public class Server {

    public static void main(String[] args){


        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setLocation(0, 0);


        JPanel panel = new JPanel();
        JTextArea text = new JTextArea(20, 20);
        text.setEditable(false);
        panel.add(text);

        frame.getContentPane().add(panel);

        text.append("Server started\n");

        frame.pack();
        frame.setVisible(true);

        int port = Integer.parseInt(args[0]);

        try(
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true)

        ) {

            text.append("Socket connected\n");
            String outputLine = "Hello, " + LocalDate.now();

            out.write(outputLine);
            text.append("Message sent\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

        text.append("Server closed\n");

    }



}
