package com.upec.peers.network.protocol;

import com.upec.peers.network.utils.Creator;
import com.upec.peers.network.objects.PeerAddress;
import com.upec.peers.network.utils.Serializable;
import com.upec.peers.network.nio.SerializerBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListOfPeersResponse implements Serializable {

	public static final byte ID = (byte)4;

	private Collection<PeerAddress> peerAddresses;

	public static final Creator<ListOfPeersResponse> creator = serializerBuffer -> {
		int listLength = serializerBuffer.readInt();
		List<PeerAddress> peerAddresses = new ArrayList<>();
		for (int i = 0; i < listLength; i++) {
			var port = serializerBuffer.readInt();
			var address = serializerBuffer.readString();
			peerAddresses.add(new PeerAddress(port, address));
		}
		return new ListOfPeersResponse(peerAddresses);
	};

	public ListOfPeersResponse(Collection<PeerAddress> peerAddresses) {
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

	public Collection<PeerAddress> getPeerAddresses() {
		return peerAddresses;
	}

	public void setPeerAddresses(List<PeerAddress> peerAddresses) {
		this.peerAddresses = peerAddresses;
	}

	@Override
	public String toString() {
		return peerAddresses.toString();
	}
}
