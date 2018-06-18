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
            out.println(text);
            out.flush();
    }

    @Override
    public void run() {

        try{

            out = getWriter(this.socket);
            sendToClient("You are client number " + clientList.size());
            sendToAll("Client " + clientList.size() + " has joined");

            while (shouldRun) {

                BufferedReader in = getReader(this.socket);

                String fromClient;

                if((fromClient = in.readLine()) != null){
                    sendToAll(fromClient);
                }

            }

        } catch (SocketException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
