package com.clouway.downloadagent;

public class DownloadProgress implements ProgressListener {

    @Override
    public void updateOnProgress(int progressPercent) {
        System.out.println("Progress: " + progressPercent + "%");
    }
}
