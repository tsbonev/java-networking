package com.clouway.clientserver;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args){


        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setLocation(0, 0);


        JPanel panel = new JPanel();
        JTextArea text = new JTextArea(20, 20);
        text.setEditable(false);
        panel.add(text);

        frame.getContentPane().add(panel);

        text.append("Client started\n");

        frame.pack();
        frame.setVisible(true);

        int port = Integer.parseInt(args[1]);
        String host = args[0];


        try(
                Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
                ) {

            text.append("Connected to server\n");
            String fromServer;

            while ((fromServer =  in.readLine()) != null){
                text.append("Received message from server\n");
                System.out.println(fromServer);
                text.append("Printed message to console\n");

                break;
            }

            System.out.println("Closing connection");
            text.append("Client closed");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
