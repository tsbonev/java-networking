package com.clouway.clienttracker;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.*;
import java.net.Socket;

public class ServerListener extends AbstractExecutionThreadService {

    Socket socket;
    private BufferedReader in;

    public ServerListener(Socket socket) {
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
    protected BufferedReader getReader(Socket socket) throws IOException {

        return new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }


    @Override
    protected void startUp() throws IOException {
        this.in = getReader(socket);
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

            PrintWriter stdOut =
                    new PrintWriter(new OutputStreamWriter(System.out));
            String fromServer;

            while ((fromServer = in.readLine()) != null && !fromServer.equalsIgnoreCase("")){

                stdOut.println(fromServer);
                stdOut.flush();

            }

        }
    }
}
