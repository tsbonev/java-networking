package com.clouway.clientserver;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ClientTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
    }

    @Test
    public void clientHasSocketAndFrame() throws InterruptedException {

        Socket socket = new Socket();

        JTextArea text = new JTextArea(){
            @Override
            public void append(String text){
                System.out.print(text);
            }
        };

        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
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

        client.setFrame(text);

        Thread clientThread = new Thread(client);

        clientThread.start();

        clientThread.join();

        assertThat(outContent.toString().split("\n").length, is(7));

    }


}
