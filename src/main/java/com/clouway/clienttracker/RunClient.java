package com.clouway.clienttracker;

public class RunClient {

    public static void main(String[] args){

        Client client = new Client("localhost", 4444);

        Thread clientThread = new Thread(client);

        clientThread.start();

    }

}
