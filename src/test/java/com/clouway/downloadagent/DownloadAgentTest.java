package com.clouway.downloadagent;

import org.apache.commons.io.FileUtils;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class DownloadAgentTest {


    DownloadAgent agent = new DownloadAgent(new DownloadProgress());

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Before
    public void captureOut(){
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreOut(){
        System.setOut(System.out);
    }

    @Test
    public void acceptsValidUrlString() throws MalformedURLException {

        assertThat(agent.setUrl("http://www.example.com"), is(true));

    }

    @Test(expected = MalformedURLException.class)
    public void rejectsInvalidUrlString() throws MalformedURLException{

        agent.setUrl("this is not an url");

    }

    @Test
    public void acceptsValidUrlObject() throws MalformedURLException {

        URL url = new URL("http://www.example.com");

        assertThat(agent.setUrl(url), is(true));

    }

    @Test (expected = MalformedURLException.class)
    public void rejectsInvalidUrlObject() throws MalformedURLException {

        URL url = null;

        assertThat(agent.setUrl(url), is(false));

    }

    @Test
    public void acceptValidFile() throws MalformedURLException {

        File file = new File("src/test/resources/text.txt");

        assertThat(agent.setUrl(file), is(true));

    }

    @Test
    public void downloadsValidLargeFile() throws IOException {

        File fileIn = new File("src/test/resources/large.txt");
        File fileOut = new File("src/test/resources/download.txt");

        agent.setUrl(fileIn);
        agent.setOutput(fileOut);

        agent.downloadFile();

        assertThat(FileUtils.contentEquals(fileIn, fileOut), is(true));

    }

    @Test
    public void downloadPrintsProgress() throws IOException {

        ProgressListener listener = context.mock(ProgressListener.class);

        String content = "This is test content";

        InputStream inputStream = new ByteArrayInputStream(content.getBytes());

        BufferedInputStream in = new BufferedInputStream(inputStream);

        File file = new File("src/test/resources/download.txt");

        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file)){
            @Override
            public void write(int b){
                return;
            }
        };

        DownloadAgent agent = new DownloadAgent(listener){

            @Override
            protected BufferedInputStream getInputStream(){
                return in;
            }

            @Override
            protected BufferedOutputStream getOutStream(){
                return out;
            }

        };

        context.checking(new Expectations(){{

            allowing(listener).updateOnProgress(with(any(Integer.class)));

        }});


        agent.setOutput(file);

        agent.downloadFile();

        assertThat(outContent.toString().startsWith("Starting download...\n"), is(true));
        assertThat(outContent.toString().endsWith("Download finished!\n"), is(true));

    }


}
