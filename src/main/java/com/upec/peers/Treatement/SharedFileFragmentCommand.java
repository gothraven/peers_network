package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;

public class SharedFileFragmentCommand {

	public static class Request {
		public static byte ID = 0x07;

		public static ByteBuffer serialize(String filename, long size, long offset, int length) {
			SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(512));
			searlizerBuffer.writeByte(ID);
			searlizerBuffer.writeString(filename);
			searlizerBuffer.writeLong(size);
			searlizerBuffer.writeLong(offset);
			searlizerBuffer.writeInt(length);
			return searlizerBuffer.getByteBuffer();
		}

		public static void deserialize(ByteBuffer byteBuffer) {
			SerializerBuffer serializerBuffer = new SerializerBuffer(byteBuffer);
			var filename = serializerBuffer.readString();
			var size = serializerBuffer.readLong();
			var offset = serializerBuffer.readLong();
			var length = serializerBuffer.readInt();
			// return a FileFragment
		}

	}

	public static class Response {
		public static byte ID = 0x08;

		public static ByteBuffer serialize(String filename, long size, long offset, int length/* BLOB HERE*/) {
			SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(512));
			searlizerBuffer.writeByte(ID);
			searlizerBuffer.writeString(filename);
			searlizerBuffer.writeLong(size);
			searlizerBuffer.writeLong(offset);
			searlizerBuffer.writeInt(length);
			/* BLOB HERE*/
			return searlizerBuffer.getByteBuffer();
		}

		public static void deserialize(ByteBuffer byteBuffer) {
			SerializerBuffer serializerBuffer = new SerializerBuffer(byteBuffer);
			var filename = serializerBuffer.readString();
			var size = serializerBuffer.readLong();
			var offset = serializerBuffer.readLong();
			var length = serializerBuffer.readInt();
			/* BLOB HERE*/
			// return a FileFragment
		}
	}

	public static void main(String[] args) {

	}
}
