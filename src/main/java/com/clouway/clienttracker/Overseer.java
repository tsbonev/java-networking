package com.clouway.clienttracker;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.IOException;
import java.util.List;

public class Overseer extends AbstractExecutionThreadService {


    private Server server;
    private List<ClientHandler> clientList;

    public Overseer(Server server, List<ClientHandler> clientList) {
        this.server = server;
        this.clientList = clientList;
    }

    @Override
    protected void run() throws IOException {

        server.awaitRunning();
        server.awaitTerminated();

        for (ClientHandler handler : clientList) {
            handler.socket.close();
            handler.triggerShutdown();
        }

        this.stopAsync();

    }

}
