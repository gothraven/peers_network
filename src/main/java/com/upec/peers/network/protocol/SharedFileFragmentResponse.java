package com.upec.peers.network.protocol;

import com.upec.peers.network.utils.Creator;
import com.upec.peers.network.utils.Serializable;
import com.upec.peers.network.nio.SerializerBuffer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class SharedFileFragmentResponse implements Serializable {

	public static final byte ID = (byte)8;

	private String filename;
	private long size;
	private long offset;
	private int length;
	private ByteBuffer blob;

	public static final Creator<SharedFileFragmentResponse> creator = serializerBuffer -> {
		var filename = serializerBuffer.readString();
		var size = serializerBuffer.readLong();
		var offset = serializerBuffer.readLong();
		var length = serializerBuffer.readInt();
		var blob = serializerBuffer.readToByteBuffer(length);
		return new SharedFileFragmentResponse(filename, size, offset, length, blob);
	};

	public SharedFileFragmentResponse(String filename, long size, long offset, int length, ByteBuffer blob) {
		this.filename = filename;
		this.size = size;
		this.offset = offset;
		this.length = length;
		this.blob = blob;
	}

	@Override
	public SerializerBuffer serialize() {
		SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(
				1 + 4 + filename.getBytes().length + 8 + 8 + 4 + length
		));
		searlizerBuffer.writeByte(ID);
		searlizerBuffer.writeString(filename);
		searlizerBuffer.writeLong(size);
		searlizerBuffer.writeLong(offset);
		searlizerBuffer.writeInt(length);
		searlizerBuffer.writeByteBuffer(blob);
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

	public ByteBuffer getBlob() {
		return blob;
	}

	@Override
	public String toString() {
		var blob = Charset.defaultCharset().decode(this.blob);
		return filename + ", size: " + size + ", offset: " + offset + ", length: " + length + ", blob : [" + blob;
	}
}
