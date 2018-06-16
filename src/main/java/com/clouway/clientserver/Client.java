package com.clouway.clientserver;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{

    private int port;
    private String host;
    private JTextArea text;


    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    /**
     * Returns an instance of a socket.
     *
     * @param host to be used
     * @param port to be used
     * @return
     * @throws IOException
     */
    protected Socket getSocket(String host, int port) throws IOException {

        return new Socket(host, port);

    }

    /**
     * Returns a buffered reader from the
     * socket's input stream.
     *
     * @param socket to get the stream from
     * @return
     * @throws IOException
     */
    protected BufferedReader getReader(Socket socket) throws IOException {

        if(null == socket) throw new ConnectException();

        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Sets this class' JTextArea to a JTextArea that
     * has previously been linked to a JFrame.
     *
     * @param text JTextArea that has been linked to a JFrame
     */
    public void setFrame(JTextArea text){

        this.text = text;
    }

    @Override
    public void run() {

        text.append("Started client\n");

        try(
                Socket socket = getSocket(host, port);
                BufferedReader in = getReader(socket)
        ) {

            if(socket == null) throw new UnknownHostException();

            text.append("Connected to server\n");
            String fromServer;

            while ((fromServer =  in.readLine()) != null){
                text.append("Received message from server\n");
                System.out.println(fromServer);
                text.append("Printed message to console\n");

                break;
            }

            System.out.println("Closing connection");
            text.append("Client closed");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
