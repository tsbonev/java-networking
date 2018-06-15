package com.clouway.example;

import java.net.*;
import java.io.*;

public class KnockKnockServer {

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                //Create a serverSocket with a given port
                //By default this serverSocket has a host of localhost
                Socket clientSocket = serverSocket.accept();
                //Create a socket for the client to attach to
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()))
                //Create streams for communication  with client
        ) {

            String inputLine, outputLine;
            //Two holder variables

            // Initiate conversation with client
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            //Protocol handles the inputs and outputs of the server
            //Server just sends them

            outputLine = kkp.processInput(null);
            //Start by processing nothing
            out.println(outputLine);
            //And sending out the outputLine that Protocol gives us

            while ((inputLine = in.readLine()) != null) {
                //While the client is sending
                outputLine = kkp.processInput(inputLine);
                //process what is sent
                out.println(outputLine);
                //sent what is processed
                if (outputLine.equals("Bye."))
                    //If the protocol returns Bye. break
                    break;
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}