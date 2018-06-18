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

            ServerSocket serverSocket = getSocket(port);

            while (shouldRun) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(this.clientList, clientSocket);
                this.clientList.add(handler);
                handler.start();
            }

        }  catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
