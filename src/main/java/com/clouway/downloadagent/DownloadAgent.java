package com.clouway.downloadagent;

import com.sun.org.apache.xerces.internal.util.URI;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadAgent {


    private URL url;

    public boolean setUrl(String s) throws MalformedURLException {

        this.url = new URL(s);
        return true;

    }

    public boolean setUrl(URL u) throws MalformedURLException {

        if(null == u){
            throw new MalformedURLException();
        }

        this.url = u;
        return true;

    }

    public boolean setUrl(File f) throws MalformedURLException {

        if(!f.exists()) return false;

        this.url = f.toURI().toURL();
        return true;

    }

    public File downloadFile(int connectionTimeout, int readTimeout) throws IOException {

        File downloadedFile = new File("src/test/resources/download.txt");

        FileUtils.copyURLToFile(url, downloadedFile, connectionTimeout, readTimeout);

        return downloadedFile;
    }
}
