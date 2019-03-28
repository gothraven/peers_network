package com.upec.peers.network.protocol;

import com.upec.peers.network.utils.Creator;
import com.upec.peers.network.utils.Serializable;
import com.upec.peers.network.nio.SerializerBuffer;

import java.nio.ByteBuffer;

public class ListOfPeersRequest implements Serializable {

	public static final byte ID = 0x03;

	public static final Creator<ListOfPeersRequest> creator = byteBuffer -> new ListOfPeersRequest();

	public ListOfPeersRequest() {}

	@Override
	public SerializerBuffer serialize() {
		SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(1));
		searlizerBuffer.writeByte(ID);
		return searlizerBuffer;
	}
}
