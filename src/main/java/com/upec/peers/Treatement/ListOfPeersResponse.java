package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ListOfPeersResponse implements Serializable {

	public static byte ID = 0x04;

	private List<PeerAddress> peerAddresses;

	public static Creator<ListOfPeersResponse> creator = serializerBuffer -> {
		int listLength = serializerBuffer.readInt();
		List<PeerAddress> peerAddresses = new ArrayList<>();
		for (int i = 0; i < listLength; i++) {
			var port = serializerBuffer.readInt();
			var address = serializerBuffer.readString();
			peerAddresses.add(new PeerAddress(port, address));
		}
		return new ListOfPeersResponse(peerAddresses);
	};

	public ListOfPeersResponse(List<PeerAddress> peerAddresses) {
		this.peerAddresses = peerAddresses;
	}

	@Override
	public SerializerBuffer serialize() {
		SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(512));
		searlizerBuffer.writeByte(ID);
		searlizerBuffer.writeInt(peerAddresses.size());
		for (PeerAddress peerAddress : peerAddresses) {
			searlizerBuffer.writeInt(peerAddress.getPort());
			searlizerBuffer.writeString(peerAddress.getUrl());
		}
		return searlizerBuffer;
	}

	public List<PeerAddress> getPeerAddresses() {
		return peerAddresses;
	}

	public void setPeerAddresses(List<PeerAddress> peerAddresses) {
		this.peerAddresses = peerAddresses;
	}

}
