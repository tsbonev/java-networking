package com.clouway;

import com.clouway.downloadagent.DownloadAgent;
import com.clouway.downloadagent.DownloadProgress;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException {

        URL url = new URL("http://google.com");

        //File fileIn = new File("src/test/resources/large.txt");
        File fileOut = new File("src/test/resources/download.txt");

        DownloadProgress listener = new DownloadProgress();

        DownloadAgent agent = new DownloadAgent(listener);


        agent.setUrl(url);

        //agent.setUrl(fileIn);
        agent.setOutput(fileOut);

        agent.downloadFile();

    }
}
