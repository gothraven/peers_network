package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

public class ListOfPeersRequest implements Serializable {

	public static byte ID = 0x03;

	public Creator<ListOfPeersRequest> creator = byteBuffer -> new ListOfPeersRequest();

	public ListOfPeersRequest() {

    }

    public static void main(String[] args) {
//        new ListOfPeersRequest();
//        var t = Response.serialize(List.of(
//                new PeerAddress(12, "someurl1"),
//                new PeerAddress(13, "someurl2"))
//        );
//        t.rewind();
//        var id = t.get();
//        assert id == Response.ID;
//        System.out.println(Response.deserialize(t));
    }

	@Override
	public SerializerBuffer serialize() throws BufferOverflowException {
		SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(1));
		searlizerBuffer.writeByte(ID);
		return searlizerBuffer;
	}
}
