package com.clouway.clienttracker;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class TrackingClientTest {

    final int port = 4455;
    final String host = "localhost";

    ByteArrayOutputStream socketOut = new ByteArrayOutputStream();
    ByteArrayInputStream socketIn = new ByteArrayInputStream("signal".getBytes());

    Socket socket = new Socket() {

        @Override
        public InputStream getInputStream() {

            return socketIn;

        }

        @Override
        public OutputStream getOutputStream() {

            return socketOut;

        }

    };

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }


    @Test
    public void clientShouldStopWithGenerator() throws InterruptedException {

        HeartbeatGenerator hbGenerator = new HeartbeatGenerator(socket){

            @Override
            protected int getBeatDelay(){
                return 100;
            }

        };

        Client client = new Client(host, port){

            @Override
            protected Socket getSocket(String host, int port){
                return socket;
            }

            @Override
            protected void startMessenger(Socket socket){
                return;
            }

            @Override
            protected void startListener(Socket socket){
                return;
            }

            @Override
            protected void startGenerator(Socket socket){
                this.generator = hbGenerator;
            }

        };

        hbGenerator.startAsync().awaitRunning();
        client.startAsync().awaitRunning();

        assertThat(hbGenerator.isRunning(), is(true));
        assertThat(client.isRunning(), is(true));

        hbGenerator.stopAsync();

        Thread.sleep(1);

        assertThat(errContent.toString().contains("NoSocketException"), is(true));
        assertThat(outContent.toString().contains("Stopping...\n"), is(true));
        assertThat(client.isRunning(), is(false));

    }

}
