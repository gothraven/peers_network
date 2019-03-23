package com.upec.peers.Server;


import java.io.IOException;
import java.net.Socket;

/**
 * PeerConnection is a class to handle connection to another peer
 *
 * @version 1
 */
public class PeerConnection implements Runnable {

	private PeersConnectingManager context;
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
	}

	@Override
	public void run() {
		while (this.running) {
			// todo send commands in queue
//			while(!queue.isEmpty()) {
//				os.print(queue.poll());
//			}
			// keep writing in the client
//			try {
//				var in = new PrintWriter(this.socket.getOutputStream(), true);
//				in.println("stuff");
//				in.flush();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			// wait to be notified about new messages
//			try {
//				wait();
//			} catch (InterruptedException e) {
//				terminate();
//			}
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
