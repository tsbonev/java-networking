package com.clouway;

import com.clouway.clientserver.Client;
import com.clouway.clientserver.ClientServer;
import com.clouway.clientserver.Server;

public class Main {

    public static void main(String[] args) {


        Client client = new Client(4444, "localhost");
        Server server = new Server(4444);

        ClientServer clientServer = new ClientServer(client, server);

        clientServer.start();

    }
}
