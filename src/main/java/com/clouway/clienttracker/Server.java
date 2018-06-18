package com.clouway.clienttracker;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server extends AbstractExecutionThreadService {

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
    protected void startUp() throws Exception{
        serverSocket = getSocket(port);
    }

    @Override
    protected void triggerShutdown(){
        close();
    }


    @Override
    protected void run() {

        try {

            Overseer overseer = new Overseer(this, clientList);
            overseer.startAsync();

            while (shouldRun) {
                clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(this.clientList, clientSocket);
                this.clientList.add(handler);
                handler.startAsync();
                System.out.println("New client has joined");
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
            for (ClientHandler handler : clientList) {
                handler.stopAsync();
            }
            clientSocket.close();
            serverSocket.close();
            shouldRun = false;
            this.stopAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}