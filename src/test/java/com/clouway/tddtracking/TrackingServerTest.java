package com.clouway.tddtracking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class TrackingServerTest {

    final int port = 4444;

    final Socket socket = new Socket();

    final ServerSocket serverSocket = new ServerSocket(port) {
        @Override
        public Socket accept() {

            return socket;

        }
    };

    final Server server = new Server(port);

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
    public void serverShouldStart() {

        server.startAsync().awaitRunning();

        assertThat(server.isRunning(), is(true));

        server.stopAsync();

    }

    @Test
    public void serverShouldAcceptConnections() throws InterruptedException {

        Server server = new Server(port) {
            @Override
            protected ServerSocket getServerSocket(int port) {
                return serverSocket;
            }
        };


        server.startAsync().awaitRunning();

        Thread.sleep(1);

        assertThat(outContent.toString().contains("Client connected"), is(true));

        server.stopAsync();

    }

    @Test
    public void serverShouldStartClientThread() throws InterruptedException {


        Socket socket = new Socket();

        List<ClientThread> clientThreadList = new ArrayList<>();
        ClientThread cThread = new ClientThread(socket, clientThreadList);

        Server server = new Server(port) {
            @Override
            protected ServerSocket getServerSocket(int port) {
                return serverSocket;
            }

            @Override
            protected void startClientThread(Socket socket, List<ClientThread> list) {
                cThread.stopAsync().awaitRunning();
                clientThreadList.add(cThread);
            }
        };

        server.startAsync().awaitRunning();

        Thread.sleep(1);

        assertThat(cThread.isRunning(), is(true));

    }

    @Test
    public void serverShouldKillClientThreads() throws InterruptedException {

        Socket socket = new Socket();

        List<ClientThread> threadList = new ArrayList<>();
        ClientThread cThread = new ClientThread(socket, threadList);
        cThread.startAsync().awaitRunning();

        Server server = new Server(port) {
            @Override
            protected ServerSocket getServerSocket(int port) {
                return serverSocket;
            }

            @Override
            protected void startClientThread(Socket socket, List<ClientThread> list) {
                this.clientThreadList = threadList;
            }
        };

        server.startAsync().awaitRunning();

        Thread.sleep(1);

        assertThat(cThread.isRunning(), is(true));

        server.stopAsync();

        Thread.sleep(1);

        assertThat(server.isRunning(), is(false));
        assertThat(cThread.isRunning(), is(false));

    }


}
