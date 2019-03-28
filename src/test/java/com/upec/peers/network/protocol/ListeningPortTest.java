package com.upec.peers.network.protocol;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ListeningPortTest {

    @Test
    public void serialzePortRequest() {
        var port = 2020;
        var portSerialized = new ListeningPort(port);
        var sb = portSerialized.serialize();
        sb.getByteBuffer().flip();
        assertEquals(sb.readByte(), ListeningPort.ID);
        assertEquals(sb.readInt(), port);
    }


    @Test
    public void deserialzePortRequest() {
        var port = 2020;
        var portSerialized = new ListeningPort(port);
        var sb = portSerialized.serialize();
        sb.getByteBuffer().flip();
        sb.readByte();
        var portRequest = sb.readObject(ListeningPort.creator);
        assertEquals(portRequest.getPort(), port);

    }

}