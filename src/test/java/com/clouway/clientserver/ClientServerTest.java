package com.clouway.clientserver;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ClientServerTest {

    ClientServer cs = new ClientServer();

    @Test
    public void startClient(){

        cs.startClient("localhost", 4444);

    }

}
