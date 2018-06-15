package com.clouway.clientserver;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

@SuppressWarnings("Duplicates")
public class Client implements Runnable{


    private int port;
    private String host;
    private JTextArea text;

    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public void setFrame(JTextArea text){
        this.text = text;
    }

    @Override
    public void run() {

        text.append("Started client\n");

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
