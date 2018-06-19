package com.clouway.tddtracking;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends AbstractExecutionThreadService {

    private int port;
    private ServerSocket serverSocket;
    protected List<ClientThread> clientThreadList;
    private ClientThread cThread;

    public Server() {
        this.clientThreadList = new ArrayList<>();
    }

    public Server(int port) {
        this();
        this.port = port;
    }

    protected ServerSocket getServerSocket(int port) throws IOException {

        return new ServerSocket(port);

    }

    protected void startClientThread(Socket socket, List<ClientThread> list) {

        cThread = new ClientThread(socket, list);
        this.clientThreadList.add(cThread);
        cThread.startAsync().awaitRunning();

    }

    @Override
    protected void startUp() {
        try {
            this.serverSocket = getServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void triggerShutdown(){

        cThread.stopAsync();

        for (ClientThread client : clientThreadList) {
            client.stopAsync();
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void run() throws IOException {

        while (true) {

            Socket socket = serverSocket.accept();
            startClientThread(socket, clientThreadList);
            System.out.println("Client connected");

        }
    }
}
