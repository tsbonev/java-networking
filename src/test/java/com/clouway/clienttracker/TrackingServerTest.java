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

    @Test
    public void handlerPrintsWhenOtherHandlersStart() throws InterruptedException {

        ClientHandler handler;

        List<ClientHandler> list = new ArrayList<>();

        handler = new ClientHandler(list, socket);

        for(int i = 0; i < 5; i++){
            list.add(handler);
        }

        handler.startAsync().awaitRunning();

        Thread.sleep(1);

        assertThat(socketOut.toString().split("\n").length, is(11));

        handler.stopAsync();

    }

    @Test
    public void serverKillsHandlersOnShutdown() throws InterruptedException {

        List<ClientHandler> handlerList = new ArrayList<>();

        ClientHandler handler = new ClientHandler(handlerList, socket);

        handlerList.add(handler);

        handler.startAsync().awaitRunning();

        Server server = new Server(port){

            @Override
            protected ServerSocket getSocket(int port){
                return serverSocket;
            }

            @Override
            protected void setHandler(List<ClientHandler> list, Socket socket){
                this.clientList = handlerList;
                return;
            }

        };

        server.startAsync().awaitRunning();

        Thread.sleep(1);

        assertThat(handler.isRunning(), is(true));

        server.stopAsync();

        Thread.sleep(1);

        assertThat(server.isRunning(), is(false));
        assertThat(handler.isRunning(), is(false));

    }

}
