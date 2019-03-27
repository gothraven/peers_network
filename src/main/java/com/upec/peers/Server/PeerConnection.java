package com.upec.peers.Server;


import com.upec.peers.Treatement.*;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

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
	 * @param inetAddress the url of the peer server to connect
	 * @param port        the port which it listens to connection from
	 */
	PeerConnection(PeersConnectingManager context, String inetAddress, int port) throws IOException {
		this.context = context;
		this.inetAddress = inetAddress;
		this.port = port;
//		this.queue = new ConcurrentLinkedQueue<Command>(); todo enable this after adding commands
		this.socket = new Socket(inetAddress, port);
		this.running = this.socket.isConnected();
		this.in = Channels.newChannel(this.socket.getInputStream());
		this.out = Channels.newChannel(this.socket.getOutputStream());
	}

	private void read() {

	}

	@Override
	public void run() {
		while (this.running) {
			try {
				ByteBuffer bb = ByteBuffer.allocateDirect(512);
				in.read(bb);
				bb.rewind();
				var id = bb.get();
				if (id == MessageCommand.ID) {
					String message = MessageCommand.deserialize(bb);
					System.out.println(message);
				} else if (id == ListOfPeersRequest.Response.ID) {
					List<PeerAddress> list = ListOfPeersRequest.Response.deserialize(bb);
					System.out.println(list);
				} else if (id == ListOfSharedFilesCommand.Response.ID) {
					List<SharedFile> list = ListOfSharedFilesCommand.Response.deserialize(bb);
					System.out.println(list);
				}

				ByteBuffer command = ListOfSharedFilesCommand.Request.serialize();
				command.flip();
				out.write(command);

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
