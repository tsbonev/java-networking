package com.clouway.clientserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ServerTest {

    //region Stub Setup

    ByteArrayOutputStream socketOut = new ByteArrayOutputStream();


    Socket socket = new Socket(){

        @Override
        public OutputStream getOutputStream(){
            return socketOut;
        }

    };

    ServerSocket serverSocket;

    {
        try {
            serverSocket = new ServerSocket() {

                    @Override
                    public Socket accept() {
                        return socket;
                        }

                    };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    JTextArea text = new JTextArea(){
        @Override
        public void append(String text){
            System.out.print(text);
        }
    };

    Server server = new Server(4444){

        @Override
        protected ServerSocket getSocket(int port){
            return serverSocket;
        }

    };


    //endregion

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
    public void serverSendsMessage() throws InterruptedException {

        server.setFrame(text);

        Thread serverThread = new Thread(server);

        serverThread.start();

        serverThread.join();

        assertThat(socketOut.toString().startsWith("Hello,"), is(true));
        assertThat(outContent.toString().split("\n").length, is(4));
        assertThat(errContent.toString().isEmpty(), is(true));

    }

    @Test
    public void serverWaitsForClient(){
        


    }

}
