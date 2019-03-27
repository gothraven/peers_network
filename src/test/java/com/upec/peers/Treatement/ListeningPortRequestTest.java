package com.upec.peers.Treatement;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ListeningPortRequestTest {

    @Test
    public void serialzePortRequest() {
        var port = 2020;
        var portSerialized = new ListeningPortRequest(port);
        var sb = portSerialized.serialize();
        sb.getByteBuffer().flip();
        assertEquals(sb.readByte(), ListeningPortRequest.ID);
        assertEquals(sb.readInt(), port);
    }


    @Test
    public void deserialzePortRequest() {
        var port = 2020;
        var portSerialized = new ListeningPortRequest(port);
        var sb = portSerialized.serialize();
        sb.getByteBuffer().flip();
        sb.readByte();
        var portRequest = sb.readObject(ListeningPortRequest.creator);
        assertEquals(portRequest.getPort(), port);

    }

}