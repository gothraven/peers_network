package com.upec.peers.Server;

import com.upec.peers.Treatement.PeerAddress;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Consumer;

public class SerializerBuffer {

	final static Charset charset = Charset.forName("UTF-8");

	private ByteBuffer byteBuffer;

	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}

	public SerializerBuffer(ByteBuffer byteBuffer) {
		this.byteBuffer = byteBuffer;
	}

	public void writeInt(int i) {
		byteBuffer.putInt(i);
	}

	public int readInt() {
		return byteBuffer.getInt();
	}

	public void writeFloat(double d) {
		byteBuffer.putDouble(d);
	}

	public double readDouble() {
		return byteBuffer.getDouble();
	}

	public void writeByte(byte b) {
		byteBuffer.put(b);
	}

	public byte readByte() {
		return byteBuffer.get();
	}

	public void writeLong(Long l) {
		byteBuffer.putLong(l);
	}

	public Long readLong() {
		return byteBuffer.getLong();
	}

	public void writeString(String s) {
		ByteBuffer bs = charset.encode(s);
		byteBuffer.putInt(bs.remaining());
		byteBuffer.put(bs);
	}

	public String readString() {
		int n = byteBuffer.getInt();
		int lim = byteBuffer.limit();
		byteBuffer.limit(byteBuffer.position() + n);
		String s = charset.decode(byteBuffer).toString();
		byteBuffer.limit(lim);
		return s;
	}

	@Override
	public String toString() {
		return byteBuffer.toString();
	}
}
