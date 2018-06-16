package com.clouway.clientserver;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.any;

public class ClientTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void clientHasSocketAndFrame() {

        InputStream inputStream = new InputStream() {
            @Override
            public int read() {
                return 1;
            }
        };

        Socket socket = new Socket(){

            @Override
            public InputStream getInputStream(){
                return inputStream;
            }

        };

        JTextArea text = new JTextArea(){

            @Override
            public void append(String text){

            }

        };


        Client client = new Client("localhost", 4444){
            @Override
            protected Socket getSocket(String host, int port) throws IOException {
                return socket;
            }
        };

        client.setFrame(text);

        Thread clientThread = new Thread(client);

        clientThread.start();

    }


}
