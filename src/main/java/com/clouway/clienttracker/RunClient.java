package com.clouway.clienttracker;

public class RunClient {

    public static void main(String[] args){

        Client client = new Client("localhost", 4444);

        client.startAsync();

    }

}
