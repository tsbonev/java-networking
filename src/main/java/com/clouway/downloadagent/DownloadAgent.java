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
    private final int downloadRatio = 10;

    public DownloadAgent(ProgressListener listener) {
        this.listener = listener;
    }

    public boolean setUrl(String s) throws MalformedURLException {

        this.url = new URL(s);
        return true;

    }

    public boolean setUrl(URL u) throws MalformedURLException {

        if (null == u) {
            throw new MalformedURLException();
        }

        this.url = u;
        return true;

    }

    public boolean setUrl(File f) throws MalformedURLException {

        if (!f.exists()) return false;

        this.url = f.toURI().toURL();
        return true;

    }

    public boolean setOutput(File f) {

        this.outputFile = f;
        return true;

    }

    protected BufferedOutputStream getOutStream() throws FileNotFoundException {

        return new BufferedOutputStream(new FileOutputStream(outputFile));

    }

    protected BufferedInputStream getInputStream() throws IOException {

        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        return new BufferedInputStream(urlConnection.getInputStream());

    }

    public void downloadFile() {

        try (BufferedOutputStream out = getOutStream();
             BufferedInputStream in = getInputStream()) {
            long fileSize = in.available();
            int progressPercent = 0;
            percentInterval = fileSize / downloadRatio;
            long counter = 0;

            int transferData = in.read();
            System.out.println("Starting download...");
            while (transferData != -1) {

                if (++counter >= percentInterval && counter % percentInterval == 0) {
                    progressPercent = calculateProgress(progressPercent, fileSize, percentInterval);
                    listener.updateOnProgress(progressPercent);
                }
                transferData = in.read();
                out.write(transferData);
            }

            progressPercent = calculateProgress(progressPercent, fileSize, (double) counter);
            listener.updateOnProgress(progressPercent);
            System.out.println("Download finished!");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int calculateProgress(int progress, double total, double amount) {
        progress += (int) ((amount / total) * 100);
        if (progress > 100) progress = 100;
        return progress;
    }

}
