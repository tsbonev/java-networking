package com.clouway.clientserver;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;

@SuppressWarnings("Duplicates")
public class ClientServer {

    private JFrame frame;
    private JPanel panel;
    private JTextArea text;

    public void start(){

        frame = new JFrame();
        frame.setSize(500, 500);
        frame.setLocation(0, 0);


        panel = new JPanel();
        text = new JTextArea(20, 20);
        text.setEditable(false);
        panel.add(text);

        frame.getContentPane().add(panel);


        frame.pack();
        frame.setVisible(true);


        startServer(4444);
        text.append("Server started\n");

        startClient("localhost", 4444);
        text.append("Client started\n");

    }


    public void startServer(int port){

        (new Thread(() -> {
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

        })).start();

    }

    public void startClient(String host, int port){

        (new Thread(() -> {

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

        })).start();

    }

}
