package com.clouway.clienttracker;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {

    List<ClientHandler> clientList;
    Socket socket;
    boolean shouldRun = true;

    public ClientHandler(List<ClientHandler> clientList, Socket socket) {
        this.socket = socket;
        this.clientList = clientList;
    }

    protected PrintWriter getWriter(Socket socket) throws IOException {

        return new PrintWriter(socket.getOutputStream());

    }

    protected BufferedReader getReader(Socket socket) throws IOException {

        return new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }

    private void sendToAll(String text) {

        for (ClientHandler handler : clientList) {
            handler.sendToClient(text);
        }

    }

    private void sendToClient(String text) {
        try (
                PrintWriter out = getWriter(this.socket)
        ) {

            out.println(text);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try{

            BufferedReader in = getReader(this.socket);

            sendToClient("You are client number " + clientList.size());

            while (shouldRun) {

                String fromClient;

                if((fromClient = in.readLine()) != null){
                    sendToAll(fromClient);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
