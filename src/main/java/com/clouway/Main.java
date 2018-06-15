package com.clouway;

import com.clouway.clientserver.Client;
import com.clouway.clientserver.ClientServer;
import com.clouway.clientserver.Server;

public class Main {

    public static void main(String[] args) {


        Client client = new Client("localhost", 4444);
        Server server = new Server(4444);

        ClientServer clientServer = new ClientServer(client, server);

        clientServer.start();

    }
}
