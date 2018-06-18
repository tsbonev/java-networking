package com.clouway.clienttracker;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class TrackingServerTest {

    Socket socket = new Socket();

    ServerSocket serverSocket = new ServerSocket(){

        @Override
        public Socket accept(){
            return socket;
        }

    };

    final int port = 4455;
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
    public void shouldAcceptConnection(){

        server.startAsync().awaitRunning();

        assertThat(server.isRunning(), is(true));
        assertThat(outContent.toString().contains("New client has joined"), is(true));

    }

}
