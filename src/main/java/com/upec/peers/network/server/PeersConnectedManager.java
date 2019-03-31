package com.upec.peers.network.server;

import com.upec.peers.network.database.DataBase;
import com.upec.peers.network.objects.PeerAddress;
import com.upec.peers.network.protocol.*;
import com.upec.peers.network.utils.ClientNotActive;
import com.upec.peers.network.utils.ServerListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeersConnectedManager implements Runnable {

	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
	private DataBase dataBase;
	private HashMap<SocketChannel, PeerConnected> connectedPeers;
	private ServerListener serverListener;
	private Logger logger;
	private volatile boolean running;

	public PeersConnectedManager(int serverPort, DataBase dataBase, Logger logger) throws IOException {
		this.logger = logger;
		this.dataBase = dataBase;
		this.serverSocketChannel = ServerSocketChannel.open();
		this.selector = Selector.open();
		this.running = true;

		connectedPeers = new HashMap<>();
		SocketAddress socketAddress = new InetSocketAddress(serverPort);
		serverSocketChannel.bind(socketAddress);
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	private void accept() throws IOException {
		SocketChannel sc = serverSocketChannel.accept();
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
		connectedPeers.put(sc, new PeerConnected(sc, this));
	}

	@Override
	public void run() {
		logger.log(Level.INFO, "Server starting at : localhost:" + this.serverSocketChannel.socket().getLocalPort());
		while (this.running) {
			try {
				selector.select();
				for (SelectionKey sk : selector.selectedKeys()) {
					if (sk.isAcceptable()) {
						this.accept();
					} else if (sk.isReadable()){
						SocketChannel channel = (SocketChannel) sk.channel();
						try {
							this.connectedPeers.get(channel).read();
						} catch (ClientNotActive e) {
							e.printStackTrace();
							channel.close();
							sk.cancel();
						}
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

	public void regesterListener(ServerListener serverListener) {
		this.serverListener = serverListener;
	}

	void recievedMessage(InformationMessage message, PeerConnected peer) throws IOException {
		logger.log(Level.INFO, peer.getSocketChannel().getRemoteAddress().toString() + "'s message :");
		logger.log(Level.INFO, message.getMessage());
	}

	void recievedListeningPort(ListeningPort listeningPort, PeerConnected peer) {
		var newKnownPeer = new PeerAddress(listeningPort.getPort(), peer.getSocketChannel().socket().getInetAddress().toString());
		this.dataBase.addKnownPeer(newKnownPeer);
	}

	void recievedListOfPeersRequest(PeerConnected peer) throws IOException {
        var response = new ListOfPeersResponse(this.dataBase.getKnownPeers());
		var data = response.serialize().getByteBuffer();
		data.flip();
        peer.getSocketChannel().write(data);
	}

	void recievedListOfSharedFilesRequest(PeerConnected peer) throws IOException {
		var response = new ListOfSharedFilesResponse(this.dataBase.getSharedFiles());
		var data = response.serialize().getByteBuffer();
		data.flip();
		peer.getSocketChannel().write(data);
	}

	void recievedSharedFileFragmentRequest(SharedFileFragmentRequest sharedFileFragmentRequest, PeerConnected peer) throws IOException, URISyntaxException {
		String fileName = sharedFileFragmentRequest.getFilename();
		long size = sharedFileFragmentRequest.getSize();
		long offset = sharedFileFragmentRequest.getOffset();
		int length = sharedFileFragmentRequest.getLength();
		System.out.println("<<==" + sharedFileFragmentRequest);
		if (length <= 65536) {
			ByteBuffer blob = this.dataBase.getFileFragment(fileName, size, offset, length);
			var response = new SharedFileFragmentResponse(fileName, size, offset, length, blob);
			var data = response.serialize().getByteBuffer();
			data.flip();
			peer.getSocketChannel().write(data);
		}
	}
}
