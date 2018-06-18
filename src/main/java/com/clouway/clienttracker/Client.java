package com.clouway.clienttracker;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends AbstractExecutionThreadService {

    private int port;
    private String host;
    boolean shouldRun = true;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;

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

    /**
     * Returns a new instance of a print writer
     * from the output stream of the current socket.
     *
     * @param socket
     * @return
     * @throws IOException
     */
    protected PrintWriter getWriter(Socket socket) throws IOException {

        return new PrintWriter(socket.getOutputStream());

    }

    @Override
    protected void startUp() throws IOException {

        socket = getSocket(host, port);
        out = getWriter(socket);
        in = getReader(socket);
        stdIn = new BufferedReader(new InputStreamReader(System.in));

    }

    @Override
    protected void triggerShutdown() {
        close();
    }

    @Override
    protected void run() {

        try {

            HeartbeatGenerator generator = new HeartbeatGenerator(socket);
            generator.startAsync().awaitRunning();

            while (shouldRun) {

                if(!generator.isRunning()){
                    throw new NoSocketException();
                }

            }

        } catch (NoSocketException e) {
            e.printStackTrace();
            close();
        }
    }

    private void close() {

        try {
            out.close();
            in.close();
            socket.close();
            stdIn.close();
            shouldRun = false;
            this.stopAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

