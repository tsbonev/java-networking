package com.clouway.clientserver;

@SuppressWarnings("Duplicates")
public class ClientServer {

    private Client client;
    private Server server;

    public ClientServer(Client client, Server server) {
        this.client = client;
        this.server = server;
    }

    public void start(){

        FramePackage framePackage = SetupFrame.setupFrame();

        client.setFrame(framePackage.getText());
        server.setFrame(framePackage.getText());

        Thread clientThread = new Thread(client);
        Thread serverThread = new Thread(server);

        serverThread.start();
        clientThread.start();


    }

}
