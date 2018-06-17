package com.clouway.downloadagent;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadAgent {


    private URL url;
    private File outputFile;
    private ProgressListener listener;
    private double percentInterval;
    private final int downloadRatio = 15;

    public DownloadAgent(ProgressListener listener){
        this.listener = listener;
    }

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

    public boolean setOutput(File f) {

        this.outputFile = f;
        return true;

    }

    public void downloadFile() throws IOException {

        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try{
            out = new BufferedOutputStream(new FileOutputStream(outputFile));
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            in = new BufferedInputStream(urlConnection.getInputStream());

            long fileSize = in.available();
            int progressPercent = 0;
            percentInterval = fileSize / downloadRatio;
            long counter = 0;

            int transferData = in.read();
            System.out.println("Starting download...");
            while (transferData != -1){

                if(++counter >= percentInterval && counter % percentInterval == 0){
                    progressPercent = calculateProgress(progressPercent, fileSize, percentInterval);
                    listener.updateOnProgress(progressPercent);
                }
                transferData = in.read();
                out.write(transferData);
            }

            progressPercent = calculateProgress(progressPercent, fileSize, (double)counter);
            listener.updateOnProgress(progressPercent);
            System.out.println("Download finished");

        }finally {

            if(null != out){
                out.close();
            }
            if(null != in){
                in.close();
            }
        }
    }

    private int calculateProgress(int progress, double total, double amount){
        progress += (int) ((amount / total) * 100);
        if(progress > 100) progress = 100;
        return progress;
    }

}
