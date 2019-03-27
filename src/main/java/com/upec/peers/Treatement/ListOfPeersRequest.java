package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;

public class ListOfPeersRequest implements Serializable {

	public static byte ID = 0x03;

	public static Creator<ListOfPeersRequest> creator = byteBuffer -> new ListOfPeersRequest();

	public ListOfPeersRequest() {}

	@Override
	public SerializerBuffer serialize() {
		SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(1));
		searlizerBuffer.writeByte(ID);
		return searlizerBuffer;
	}
}
