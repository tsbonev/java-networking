package com.clouway;

import com.clouway.clienttracker.Server;

public class Main {

    public static void main(String[] args) {

        Server server = new Server(4444);

        server.startAsync();

    }
}
