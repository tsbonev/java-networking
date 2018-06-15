package com.clouway;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws MalformedURLException, URISyntaxException {

        URL myURL = new URL("http://example.com/");

        URL pageURL = new URL (myURL, "page1.html");

        URL pageBottomURL = new URL (pageURL, "#BOTTOM");

        URI url = new URI("http", "example.com", "/hello world/", "");

        URL parsedURI = url.toURL();

        System.out.println(parsedURI);

        URL aURL = new URL("http://example.com:80/docs/books/tutorial"
                + "/index.html?name=networking#DOWNLOADING");

        System.out.println("protocol = " + aURL.getProtocol());
        System.out.println("authority = " + aURL.getAuthority());
        System.out.println("host = " + aURL.getHost());
        System.out.println("port = " + aURL.getPort());
        System.out.println("path = " + aURL.getPath());
        System.out.println("query = " + aURL.getQuery());
        System.out.println("filename = " + aURL.getFile());
        System.out.println("ref = " + aURL.getRef());

    }
}
