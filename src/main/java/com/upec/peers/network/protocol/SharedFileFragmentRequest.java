package com.upec.peers.network.protocol;

import com.upec.peers.network.utils.Creator;
import com.upec.peers.network.utils.Serializable;
import com.upec.peers.network.nio.SerializerBuffer;

import java.nio.ByteBuffer;

public class SharedFileFragmentRequest implements Serializable {

	public static final byte ID = (byte)7;

	private String filename;
	private long size;
	private long offset;
	private int length;

	public static final Creator<SharedFileFragmentRequest> creator = serializerBuffer -> {
		var filename = serializerBuffer.readString();
		var size = serializerBuffer.readLong();
		var offset = serializerBuffer.readLong();
		var length = serializerBuffer.readInt();
		return new SharedFileFragmentRequest(filename, size, offset, length);
	};

	public SharedFileFragmentRequest(String filename, long size, long offset, int length) {
		this.filename = filename;
		this.size = size;
		this.offset = offset;
		this.length = length;
	}

	@Override
	public SerializerBuffer serialize() {
		SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(512));
		searlizerBuffer.writeByte(ID);
		searlizerBuffer.writeString(filename);
		searlizerBuffer.writeLong(size);
		searlizerBuffer.writeLong(offset);
		searlizerBuffer.writeInt(length);
		return searlizerBuffer;
	}

	public String getFilename() {
		return filename;
	}

	public long getSize() {
		return size;
	}

	public long getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}
}
