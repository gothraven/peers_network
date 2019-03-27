package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ListOfPeersResponse {

	public static byte ID = 0x04;

	public List<PeerAddress> peerAddresses;
	public Creator<ListOfPeersResponse> creator = serializerBuffer -> {
		var listOfPeersResponse = new ListOfPeersResponse();
		int listLength = serializerBuffer.readInt();
		for (int i = 0; i < listLength; i++) {
			var port = serializerBuffer.readInt();
			var address = serializerBuffer.readString();
			listOfPeersResponse.getPeerAddresses().add(new PeerAddress(port, address));
		}
		return listOfPeersResponse;
	};

	public ListOfPeersResponse(List<PeerAddress> peerAddresses) {
		this.peerAddresses = peerAddresses;
	}

	private ListOfPeersResponse() {
		this.peerAddresses = new ArrayList<>();
	}

	public static ByteBuffer serialize(List<PeerAddress> peersAddressList) {
		SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(512));
		searlizerBuffer.writeByte(ID);
		searlizerBuffer.writeInt(peersAddressList.size());
		for (PeerAddress peerAddress : peersAddressList) {
			searlizerBuffer.writeInt(peerAddress.getPort());
			searlizerBuffer.writeString(peerAddress.getUrl());
		}
		return searlizerBuffer.getByteBuffer();
	}

	public List<PeerAddress> getPeerAddresses() {
		return peerAddresses;
	}

	public void setPeerAddresses(List<PeerAddress> peerAddresses) {
		this.peerAddresses = peerAddresses;
	}
}
