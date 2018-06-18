package com.clouway.clienttracker;

public class RunServer {

    public static void main(String[] args) {

        Server server = new Server(4444);

        server.startAsync();
    }

}
