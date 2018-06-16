package com.clouway.clientserver;

import org.junit.Test;
import javax.swing.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ClientServerIntegrationTest {

    FramePackage framePackage = SetupFrame.setupFrame();

    JTextArea text = framePackage.getText();


    @Test
    public void serverStartsAndWaitsForClient() {

        Server server = new Server(4445);

        server.setFrame(text);

        Thread serverThread = new Thread(server);

        serverThread.start();

        assertThat(serverThread.isAlive(), is(true));

        serverThread.interrupt();

    }

    @Test
    public void serverStopsAfterConnectingToClient() throws InterruptedException {

        Client client = new Client("localhost", 4446);
        Server server = new Server(4446);

        server.setFrame(text);
        client.setFrame(text);

        Thread serverThread = new Thread(server);
        Thread clientThread = new Thread(client);
        serverThread.start();

        assertThat(serverThread.isAlive(), is(true));

        clientThread.start();

        assertThat(clientThread.isAlive(), is(true));

        serverThread.join();
        clientThread.join();

        assertThat(serverThread.isAlive(), is(false));

    }

    @Test
    public void serverWritesToJFrame() throws InterruptedException {

        Server server = new Server(4447);

        server.setFrame(text);
        Thread serverThread = new Thread(server);
        serverThread.start();

        Thread.currentThread().sleep(100);

        String log = "Started server\n";

        assertThat(text.getText().equals(log), is(true));

        serverThread.interrupt();

    }


}
