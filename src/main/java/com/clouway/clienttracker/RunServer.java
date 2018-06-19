package com.clouway.clienttracker;

public class RunServer {

    public static void main(String[] args) throws InterruptedException {

        Server server = new Server(4444);

        server.startAsync();

        Thread.sleep(5000);

    }

}
