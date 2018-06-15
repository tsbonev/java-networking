package com.clouway;

import com.clouway.downloadagent.DownloadAgent;
import sun.management.resources.agent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException {

        URL url = new URL("http://google.com");

        DownloadAgent agent = new DownloadAgent();

        agent.setUrl(url);

        agent.downloadFile(10000, 10000);

    }
}
