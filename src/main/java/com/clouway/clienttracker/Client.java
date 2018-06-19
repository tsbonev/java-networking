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
    private boolean shouldRun = true;
    private ClientMessenger messenger;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

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

    /**
     * Starts a new thread with a messenger.
     *
     * @param socket
     */
    protected void startMessenger(Socket socket) {

        messenger = new ClientMessenger(socket);
        messenger.startAsync().awaitRunning();

    }

    @Override
    protected void startUp() throws IOException {

        socket = getSocket(host, port);
        out = getWriter(socket);
        in = getReader(socket);

    }

    @Override
    protected void triggerShutdown() {
        close();
    }

    @Override
    protected void run() {

        try {

            startMessenger(socket);

            while (shouldRun) {

                String fromServer = in.readLine();

                if(fromServer != null){
                    System.out.println(fromServer);
                }else {
                    throw new NoSocketException();
                }

            }

        } catch (NoSocketException e) {
            e.printStackTrace();
                System.out.println("Stopping...");
                shouldRun = false;
                this.stopAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {

        try {
            messenger.stopAsync().awaitTerminated();
            out.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

