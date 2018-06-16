package com.clouway.clientserver;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;

public class Server implements Runnable{

    private int port;
    private JTextArea text;

    public Server(int port) {
        this.port = port;
    }

    public void setFrame(JTextArea text){
        this.text = text;
    }

    private ServerSocket getSocket(int port) throws IOException {

        return new ServerSocket(port);

    }

    @Override
    public void run() {

        text.append("Started server\n");

        try(
                ServerSocket serverSocket = getSocket(port);
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
