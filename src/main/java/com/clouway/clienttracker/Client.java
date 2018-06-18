package com.clouway.clienttracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Client implements Runnable {

    private int port;
    private String host;
    boolean shouldRun = true;


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

        return new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }

    protected PrintWriter getWriter(Socket socket) throws IOException {

        return new PrintWriter(socket.getOutputStream());

    }

    public void run() {

        try {

            Socket socket = getSocket(host, port);
            PrintWriter out = getWriter(socket);
            BufferedReader in = getReader(socket);
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            while (shouldRun) {

                String fromServer;
                String fromUser;

                while((fromServer = in.readLine()) != null) {
                    System.out.println("Server: " + fromServer);
                }

                while ((fromUser = stdIn.readLine()) != null) {
                    out.write(fromUser);
                }

            }

        }catch (SocketException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

