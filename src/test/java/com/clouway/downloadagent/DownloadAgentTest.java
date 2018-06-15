package com.clouway.downloadagent;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class DownloadAgentTest {

    DownloadAgent agent = new DownloadAgent();

    @Test
    public void acceptValidUrlString() throws MalformedURLException {

        assertThat(agent.setUrl("http://www.example.com"), is(true));

    }

    @Test(expected = MalformedURLException.class)
    public void rejectInvalidUrlString() throws MalformedURLException{

        agent.setUrl("this is not an url");

    }

    @Test
    public void acceptValidUrlObject() throws MalformedURLException {

        URL url = new URL("http://www.example.com");

        assertThat(agent.setUrl(url), is(true));

    }

    @Test (expected = MalformedURLException.class)
    public void rejectInvalidUrlObject() throws MalformedURLException {

        URL url = null;

        assertThat(agent.setUrl(url), is(false));

    }

    @Test
    public void acceptFile() throws MalformedURLException {

        File file = new File("/home/clouway/workspaces/idea/networking/src/test/resources/text.txt");

        assertThat(agent.setUrl(file), is(true));

    }

    @Test
    public void rejectNonExistentFile() throws MalformedURLException{

        File file = new File("non-existent-text.txt");

        assertThat(agent.setUrl(file), is(false));

    }

    @Test
    public void downloadFile() throws IOException {

        File file = new File("src/test/resources/text.txt");

        agent.setUrl(file);

        File download = agent.downloadFile(10000, 10000);

        assertThat(FileUtils.contentEquals(file, download), is(true));

        //така и начина по - който прогреса се обновява.
        //ask what this entails

    }

}
