package com.clouway.clienttracker;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMessenger extends AbstractExecutionThreadService {

    Socket socket;
    private PrintWriter out;

    public ClientMessenger(Socket socket) {
        this.socket = socket;
    }

    /**
     * Returns a buffered reader from the
     * socket's input stream.
     *
     * @param socket to get the stream from
     * @return
     * @throws IOException
     */
    protected PrintWriter getWriter(Socket socket) throws IOException {

        return new PrintWriter(socket.getOutputStream());

    }


    @Override
    protected void startUp() throws IOException {
        this.out = getWriter(socket);
    }

    @Override
    protected void triggerShutdown(){

        try {
            socket.close();
            this.stopAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void run() throws IOException {

        while (true)
        {

            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));

            String fromClient;

            while ((fromClient = stdIn.readLine()) != null){

                out.println(fromClient);
                out.flush();

            }

        }
    }

}
