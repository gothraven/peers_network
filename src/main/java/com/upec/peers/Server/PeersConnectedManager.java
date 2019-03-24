package com.upec.peers.Server;

import com.google.common.primitives.Bytes;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PeersConnectedManager implements Runnable {
	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
	private ByteBuffer byteBuffer;

	private volatile boolean running;

	private List<String> knownPeers;


	public PeersConnectedManager(int listeningPort) throws IOException {
		this.knownPeers = Collections.synchronizedList(new ArrayList<>());
		this.serverSocketChannel = ServerSocketChannel.open();
		this.selector = Selector.open();
		this.byteBuffer = ByteBuffer.allocateDirect(10);
		this.running = true;

		SocketAddress socketAddress = new InetSocketAddress(listeningPort);
		serverSocketChannel.bind(socketAddress);
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

	}

	private void accept() throws IOException {
		SocketChannel sc = serverSocketChannel.accept();
		System.out.println("Nouvelle connection" + sc);
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
	}

	private void read(SelectionKey sk) throws IOException {
		LinkedList<Byte> byteLinkedList = new LinkedList<>();
		SocketChannel channel = (SocketChannel) sk.channel();
		int bytesRead = channel.read(byteBuffer);

		if (bytesRead < 0) {
			System.out.println("Client Leave");
			sk.cancel();
			channel.close();
			return;
		}

		if (bytesRead == 0) return;

		while (bytesRead > 0) {
			byteBuffer.flip();
			while (byteBuffer.hasRemaining()) {
				byteLinkedList.add(byteBuffer.get());
			}
			byteBuffer.clear();
			bytesRead = channel.read(byteBuffer);
		}

		ByteBuffer request = ByteBuffer.wrap(Bytes.toArray(byteLinkedList));

		try {
			response(request, channel);
		} catch (Exception e) {
			e.printStackTrace();
//			serverError(stringWriter.toString(), channel);
		}
	}

	private void response(ByteBuffer request, SocketChannel channel) {
		Charset c = Charset.forName("UTF-8");
		System.out.println(channel.socket().getPort());
		if (request.getChar() == '\1') {
			int n = request.getInt();
			int lim = request.limit();
			request.limit(request.position() + n);
			String s = c.decode(request).toString();
			request.limit(lim);
			System.out.println("Message " + s);
		}
		writeData(request, channel);
	}

	private void writeData(ByteBuffer response, SocketChannel channel) {
		response.flip();
		while (response.hasRemaining()) {
			try {
				channel.write(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}

	@Override
	public void run() {
		while (this.running) {
			try {
				selector.select();
				for (SelectionKey sk : selector.selectedKeys()) {
					if (sk.isAcceptable()) {
						this.accept();
					} else {
						this.read(sk);
					}
				}
				selector.selectedKeys().clear();
			} catch (IOException e) {
				e.printStackTrace();
				this.terminate();
			}
		}
	}

	public void terminate() {
		this.running = false;
		try {
			this.serverSocketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		return running;
	}
}
