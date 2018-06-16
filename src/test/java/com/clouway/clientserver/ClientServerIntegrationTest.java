package com.clouway.clientserver;

import org.junit.Test;
import javax.swing.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ClientServerIntegrationTest {

    Client client = new Client("localhost", 4444);
    Server server = new Server(4444);

    FramePackage framePackage = SetupFrame.setupFrame();

    JTextArea text = framePackage.getText();


    @Test
    public void serverStartsAndWaitsForClient(){

        server.setFrame(text);

        Thread serverThread = new Thread(server);

        serverThread.start();

        assertThat(serverThread.isAlive(), is(true));

    }

    @Test
    public void serverStopsAfterConnectingToClient() throws InterruptedException {

        server.setFrame(text);
        client.setFrame(text);

        Thread serverThread = new Thread(server);
        Thread clientThread = new Thread(client);
        serverThread.start();

        assertThat(serverThread.isAlive(), is(true));

        clientThread.start();

        assertThat(clientThread.isAlive(), is(true));

        Thread.currentThread().sleep(100);

        assertThat(serverThread.isAlive(), is(false));

    }

    @Test
    public void serverWritesToJframe() throws InterruptedException {

        server.setFrame(text);
        Thread serverThread = new Thread(server);
        serverThread.start();

        Thread.currentThread().sleep(100);

        String log = "Started server\n";

        assertThat(text.getText().equals(log), is(true));

        serverThread.interrupt();

    }


}
