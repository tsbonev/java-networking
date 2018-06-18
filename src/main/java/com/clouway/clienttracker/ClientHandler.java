package com.clouway.clienttracker;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ClientHandler extends Thread {

    List<ClientHandler> clientList;
    Socket socket;
    boolean shouldRun = true;
    private PrintWriter out;
    private BufferedReader in;


    public ClientHandler(List<ClientHandler> clientList, Socket socket) {
        this.socket = socket;
        this.clientList = clientList;
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
     * Returns a new instance of a buffered reader
     * from the input stream of the current socket.
     *
     * @param socket
     * @return
     * @throws IOException
     */
    protected BufferedReader getReader(Socket socket) throws IOException {

        return new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }


    /**
     * Sends a string to all clients currently in the list.
     *
     * @param text to be sent
     */
    private void sendToAll(String text) {

        for (ClientHandler handler : clientList) {
            handler.sendToClient(text);
        }

    }

    /**
     * Sends a string to the output stream of the current socket.
     *
     * @param text to be sent
     */
    private void sendToClient(String text) {
            out.println(text);
            out.flush();
    }

    @Override
    public void run() {

        try{

            out = getWriter(this.socket);
            sendToClient("You are client number " + clientList.size());
            sendToAll("Client " + clientList.size() + " has joined");
            in = getReader(this.socket);

            while (shouldRun) {

                String fromClient;

                while ((fromClient = in.readLine()) != null){
                    sendToAll(fromClient);
                }

            }

            close();

        } catch (SocketException e){
            e.printStackTrace();
            close();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }

    }

    private void close() {

        try {
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
