package com.clouway;

import java.net.*;
import java.io.*;

public class URLReader {
    public static void main(String[] args) throws Exception {


        URL oracle = new URL("http://www.example.com/");
        
        URLConnection connection = oracle.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
        in.close();




    }
}

