package com.upec.peers.Server;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

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

	public void writeFloat(double d) {
		byteBuffer.putDouble(d);
	}

	public int readInt() {
		return byteBuffer.getInt();
	}

	public double readDouble() {
		return byteBuffer.getDouble();
	}


	public void writeChar(char c) {
		byteBuffer.putChar(c);
	}

	public char readChar() {
		return byteBuffer.getChar();
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
		byteBuffer.limit(byteBuffer.position()+n);
		String s = charset.decode(byteBuffer).toString();
		byteBuffer.limit(lim);
		return s;
	}

	@Override
	public String toString() {
		return byteBuffer.toString();
	}
}
