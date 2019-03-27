package com.upec.peers.network.client;


import com.upec.peers.network.nio.SerializerBuffer;
import com.upec.peers.network.protocol.*;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

/**
 * PeerConnection is a class to handle connection to another peer
 *
 * @version 1
 */
public class PeerConnection implements Runnable {

	private PeersConnectingManager context;
	private WritableByteChannel out;
	private ReadableByteChannel in;
	private String inetAddress;
	private int port;
	private Socket socket;
	private boolean running;
//	private ConcurrentLinkedQueue<Command> queue; todo implement Commands type to be able to send them to the other peer

	/**
	 * PeerConnection constructor
	 *
	 * @param inetAddress the url of the peer network to connect
	 * @param port        the port which it listens to connection from
	 */
	public PeerConnection(PeersConnectingManager context, String inetAddress, int port) throws IOException {
		this.context = context;
		this.inetAddress = inetAddress;
		this.port = port;
//		this.queue = new ConcurrentLinkedQueue<Command>(); todo enable this after adding commands
		this.socket = new Socket(inetAddress, port);
		this.running = this.socket.isConnected();
		this.in = Channels.newChannel(this.socket.getInputStream());
		this.out = Channels.newChannel(this.socket.getOutputStream());
	}

	@Override
	public void run() {
		while (this.running) {
			try {
				SerializerBuffer sb = new SerializerBuffer(512);
				in.read(sb.getByteBuffer());
				sb.flip();
				var id = sb.readByte();
				if (id == MessageRequest.ID) {
					var message = sb.readObject(MessageRequest.creator);
					System.out.println(message);
				} else if (id == ListOfPeersResponse.ID) {
					var response = sb.readObject(ListOfPeersResponse.creator);
					System.out.println(response.getPeerAddresses());
				} else if (id == ListOfSharedFilesResponse.ID) {
					var response = sb.readObject(ListOfSharedFilesResponse.creator);
					System.out.println(response.getListOfSharedFiles());
				} else if (id == SharedFileFragmentResponse.ID) {
					var response = sb.readObject(SharedFileFragmentResponse.creator);
					var blob = response.getBlob();
					Charset charset = Charset.forName("UTF-8");
					var result = charset.decode(blob);
					System.out.println(result);
				}

//				var command = new ListOfSharedFilesRequest();
				var command = new SharedFileFragmentRequest("amrane.txt", 22, 0, 22);
				var buffer = command.serialize();
				buffer.flip();
				out.write(buffer.getByteBuffer());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void sendCommand(/*Command command*/) {
//		queue.add(command); todo enable this after adding commands
		this.notify();
	}

	public void terminate() {
		this.running = false;
	}
}
