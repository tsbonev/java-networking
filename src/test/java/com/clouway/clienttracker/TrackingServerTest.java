package com.clouway.clienttracker;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class TrackingServerTest {

    ByteArrayOutputStream socketOut = new ByteArrayOutputStream();
    ByteArrayInputStream socketIn = new ByteArrayInputStream("signal".getBytes());

    Socket socket = new Socket(){

        @Override
        public InputStream getInputStream(){

            return socketIn;

        }

        @Override
        public OutputStream getOutputStream(){

            return socketOut;

        }

    };

    ServerSocket serverSocket = new ServerSocket(){

        @Override
        public Socket accept(){
            return socket;
        }

    };

    final int port = 4455;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    public TrackingServerTest() throws IOException {
    }

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
    public void serverShouldAcceptConnection() throws InterruptedException {

        Server server = new Server(port){

            @Override
            protected ServerSocket getSocket(int port){
                return serverSocket;
            }

            @Override
            protected void setHeartbeatListener(Socket socket){
                return;
            }

            @Override
            protected void setHandler(List<ClientHandler> list, Socket socket){
                return;
            }

        };

        server.startAsync().awaitRunning();

        Thread.currentThread().sleep(1);

        assertThat(server.isRunning(), is(true));
        assertThat(outContent.toString().contains("New client has joined"), is(true));

        server.stopAsync();

    }

    @Test
    public void heartbeatListenerShouldReceiveAndSendSignal() throws InterruptedException {

        HeartbeatListener listener = new HeartbeatListener(socket);
        listener.startAsync().awaitRunning();

        Thread.currentThread().sleep(1);

        assertThat(listener.isRunning(), is(true));
        assertThat(socketOut.toString(), is("signal\n"));

        listener.stopAsync();

    }

    @Test
    public void heartbeatGeneratorShouldSendTickAndStop() throws InterruptedException, TimeoutException {

        HeartbeatGenerator generator = new HeartbeatGenerator(socket);
        generator.startAsync().awaitRunning();

        Thread.currentThread().sleep(1);

        assertThat(socketOut.toString(), is("tick\n"));

        generator.awaitTerminated(600, TimeUnit.MILLISECONDS);

        assertThat(generator.isRunning(), is(false));
        assertThat(errContent.toString().contains("NoSocketException"), is(true));

        generator.stopAsync();

    }

    @Test
    public void handlerShouldPrintPositionAndSignal() throws InterruptedException {

        ClientHandler handler;

        List<ClientHandler> list = new ArrayList<>();

        handler = new ClientHandler(list, socket);

        list.add(handler);

        handler.startAsync().awaitRunning();

        Thread.sleep(1);

        assertThat(socketOut.toString(), is("You are client number 1\nClient 1 has joined\nsignal\n"));

        handler.stopAsync();

    }

}
