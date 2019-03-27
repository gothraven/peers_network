package com.upec.peers.Server;



import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

/**
 * BufferPool maintains a statically-allocated pool of memory buffers
 */
public class BufferPool {

	public static final int BUFFER_POOL_SIZE = 512;
	public static final int BUFFER_SIZE = 1024;

	// Note: the list is not internally thread-safe.
	// So, every read and write must be synchronized
	private final List<ByteBuffer> freeBuffers = new LinkedList<>();

	public BufferPool() {
		for (int i=0; i < BUFFER_POOL_SIZE; i++) {
			ByteBuffer buff = ByteBuffer.allocateDirect(BUFFER_SIZE);
			freeBuffers.add(buff);
		}
	}

	public synchronized int getNumFreeBuffers() {
		return freeBuffers.size();
	}

	public synchronized ByteBuffer getFreeBuffer() {
		while (freeBuffers.isEmpty()) {
			try {
				wait();
			}
			catch (InterruptedException ignored) {}
		}
		return freeBuffers.remove(0);
	}

	public synchronized void returnFreeBuffer(ByteBuffer buf) {
		buf.clear();
		freeBuffers.add(buf);
		notify();
	}
}