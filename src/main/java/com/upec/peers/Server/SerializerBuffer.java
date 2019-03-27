package com.upec.peers.Server;


import com.upec.peers.Treatement.Creator;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;


public class SerializerBuffer {

	private final static Charset charset = Charset.forName("UTF-8");

	private ByteBuffer byteBuffer;

	public SerializerBuffer(ByteBuffer byteBuffer) {
		this.byteBuffer = byteBuffer;
	}

	public void writeInt(int i) {
		byteBuffer.putInt(i);
	}

	public int readInt() {
		return byteBuffer.getInt();
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

	public String readString(){
		int size = byteBuffer.getInt();
		byte[] bytes = new byte[size];;
		byteBuffer.get(bytes);
		return new String(bytes);
	}

	public void writeByteBuffer(ByteBuffer byteBuffer) {
		byteBuffer.put(byteBuffer);
	}

	public ByteBuffer readToByteBuffer(int length) {
		byte[] bytes = new byte[length];;
		return byteBuffer.get(bytes);
	}


	public <T> T readObject(Creator<T> creator) {
		return creator.construct(this);
	}

	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}

	@Override
	public String toString() {
		return byteBuffer.toString();
	}

}
