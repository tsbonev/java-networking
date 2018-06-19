package com.clouway.clienttracker;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HeartbeatGenerator extends AbstractExecutionThreadService {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int beatDelay = 3000;

    public HeartbeatGenerator(Socket socket) {
        this.socket = socket;
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
     * Returns the beat delay of this class.
     *
     * @return
     */
    protected int getBeatDelay() {
        return this.beatDelay;
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

        this.out = getWriter(this.socket);
        this.in = getReader(this.socket);

    }

    @Override
    protected void run() throws NoSocketException {

        try {

            while (true) {
                String fromServer;
                out.print("");
                out.flush();
                Thread.sleep(getBeatDelay());
                if((fromServer = in.readLine()) == null) throw new NoSocketException();
                else if(!fromServer.contains("Client")) out.println(fromServer);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
