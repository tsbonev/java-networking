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
    protected List<ClientHandler> clientList;
    private boolean shouldRun = true;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private HeartbeatListener listener;


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

    protected void setHandler(List<ClientHandler> list, Socket socket){

        ClientHandler handler = new ClientHandler(list, socket);
        clientList.add(handler);
        handler.startAsync().awaitRunning();

    }

    protected void setHeartbeatListener(Socket socket){

        HeartbeatListener listener = new HeartbeatListener(socket);
        listener.startAsync().awaitRunning();
        this.listener = listener;

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

            while (shouldRun) {
                clientSocket = serverSocket.accept();
                setHandler(this.clientList, this.clientSocket);

                System.out.println("New client has joined");

            }


        }  catch (SocketException e) {
            e.printStackTrace();
            close();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }

    }

    public void close() {

        try {
            for (ClientHandler handler : clientList) {
                handler.stopAsync();
            }
            clientSocket.close();
            serverSocket.close();
            shouldRun = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
