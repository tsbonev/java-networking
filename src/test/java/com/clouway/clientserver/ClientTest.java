package com.clouway.clientserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ClientTest {


    //region Stub setup

    Socket socket = new Socket();

    JTextArea text = new JTextArea(){
        @Override
        public void append(String text){
            System.out.print(text);
        }
    };

    InputStream in = new InputStream() {
        @Override
        public int read() {
            return 0;
        }
    };

    Client client = new Client("localhost", 4444){

        @Override
        protected Socket getSocket(String host, int port){
            return socket;
        }

        @Override
        protected BufferedReader getReader(Socket socket){

            return new BufferedReader(new InputStreamReader(in)){

                @Override
                public String readLine(){
                    return "This is a message";
                }

            };

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
    public void clientHasSocketAndFrame() throws InterruptedException {

        client.setFrame(text);

        Thread clientThread = new Thread(client);

        clientThread.start();

        clientThread.join();

        assertThat(outContent.toString().split("\n").length, is(7));
        assertThat(errContent.toString().isEmpty(), is(true));

    }

    @Test
    public void clientHasNoSocket() throws InterruptedException {

        client.setFrame(text);

        socket = null;

        Thread clientThread = new Thread(client);

        clientThread.start();

        clientThread.join();

        assertThat(outContent.toString(), is("Started client\n"));
        assertThat(errContent.toString().isEmpty(), is(false));

    }

    @Test
    public void clientHasDisconnectedFrame() throws InterruptedException {

        client.setFrame(new JTextArea());

        Thread clientThread = new Thread(client);

        clientThread.start();

        clientThread.join();

        assertThat(outContent.toString(), is("This is a message\nClosing connection\n"));

    }

    @Test
    public void clientHasNoTextArea() throws InterruptedException {

        client.setFrame(null);

        Thread clientThread = new Thread(client);

        clientThread.start();

        clientThread.join();

        assertThat(outContent.toString().isEmpty(), is(true));
        assertThat(errContent.toString().isEmpty(), is(false));

    }

}
