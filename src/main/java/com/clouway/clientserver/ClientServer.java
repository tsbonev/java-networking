package com.clouway.clientserver;

public class ClientServer {

    private Client client;
    private Server server;

    public ClientServer(Client client, Server server) {
        this.client = client;
        this.server = server;
    }

    public void start() throws InterruptedException {

        FramePackage framePackage = SetupFrame.setupFrame();

        client.setFrame(framePackage.getText());
        server.setFrame(framePackage.getText());

        Thread clientThread = new Thread(client);
        Thread serverThread = new Thread(server);

        serverThread.start();
        Thread.currentThread().sleep(50);
        clientThread.start();


    }

}
