package com.clouway.clientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class KnockKnockClient {

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
                Socket kkSocket = new Socket(hostName, portNumber);
                //We create a socket connected to a host with a port

                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                //We open a stream writer
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(kkSocket.getInputStream()))
                //We open a buffered reader
        ) {
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            //We open a reader for the console

            String fromServer;
            String fromUser;
            //Define two holder variables

            while ((fromServer = in.readLine()) != null) {
                //While the server is sending, print
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    //If the server sends us "Bye." end
                    break;

                fromUser = stdIn.readLine();
                //read our line from the console

                if (fromUser != null) {
                    //if it isnt empty
                    System.out.println("Client: " + fromUser);
                    //print it
                    out.println(fromUser);
                    //send it to the server
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}

