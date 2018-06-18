package com.clouway.clienttracker;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {

    private int port;
    private List<ClientHandler> clientList;
    private boolean shouldRun = true;
    private ServerSocket serverSocket;
    private Socket clientSocket;


    public Server() {
        this.clientList = new ArrayList<>();
    }

    public Server(int port) {
        this();
        this.port = port;
    }

    protected ServerSocket getSocket(int port) throws IOException {
        return new ServerSocket(port);
    }


    @Override
    public void run() {

        try {

            serverSocket = getSocket(port);

            while (shouldRun) {
                clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(this.clientList, clientSocket);
                this.clientList.add(handler);
                handler.start();
            }

            close();

        }  catch (SocketException e) {
            e.printStackTrace();
            close();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }

    }

    private void close() {

        try {
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
