package com.clouway.tddtracking;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ClientThread extends AbstractExecutionThreadService {

    private Socket socket;
    private List<ClientThread> clientThreadList;

    public ClientThread(Socket socket, List<ClientThread> clientThreadList) {
        this.socket = socket;
        this.clientThreadList = clientThreadList;
    }

    @Override
    protected void triggerShutdown(){

        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void run(){

        while (true){

        }

    }
}
