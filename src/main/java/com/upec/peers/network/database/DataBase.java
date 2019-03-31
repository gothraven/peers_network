package com.upec.peers.network.database;

import com.upec.peers.interfaces.NetworkInterface;
import com.upec.peers.network.objects.PeerAddress;
import com.upec.peers.network.objects.SharedFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.io.File;

import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.ServerException;
import java.util.*;

public class DataBase {

	private static final String projectResourcesDirPath = System.getProperty("user.dir") + "/src/main/resources/";

	private static final String sharedFolderPath = projectResourcesDirPath + "shared/";
	private static final String downloadsDirPath = projectResourcesDirPath + "downloads/";

	private Set<PeerAddress> knownPeers;

	private NetworkInterface observer;

	public DataBase() {
		this.knownPeers = Collections.synchronizedSet(new HashSet<>());
	}

	public synchronized void addKnownPeer(PeerAddress newKnowPeer) {
		this.knownPeers.add(newKnowPeer);
		this.observer.listOfKnowPeersChanged(this.knownPeers);
	}

	public synchronized void addKnowPeers(Collection<PeerAddress> newKnowPeers) {
		this.knownPeers.addAll(newKnowPeers);
		this.observer.listOfKnowPeersChanged(this.knownPeers);
	}

	public synchronized Set<PeerAddress> getKnownPeers() {
		return knownPeers;
	}

	public synchronized List<SharedFile> getSharedFiles() {
		List<SharedFile> sharedFiles = new ArrayList<>();
		try {
			Files.list(new File(sharedFolderPath).toPath()).forEach(path -> {
				try {
					sharedFiles.add(new SharedFile(path.getFileName().toString(), Files.size(path)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sharedFiles;
	}

	@SuppressWarnings ("ResultOfMethodCallIgnored")
	public synchronized ByteBuffer getFileFragment(String fileName, long size, long offset, int length) throws IOException {
		byte[] bytes = new byte[length];
		URI sharedFold = new File(sharedFolderPath + fileName).toURI();
		long fileSize = Files.size(Paths.get(sharedFold));

		if (fileSize != size) throw new ServerException("File size is wrong");

		var inputStream = sharedFold.toURL().openStream();
		inputStream.skip(offset);
		inputStream.read(bytes);
		return ByteBuffer.wrap(bytes);
	}

	public synchronized void putFileFragment(String fileName, ByteBuffer Blob) throws IOException {
		Path filePath = new File(downloadsDirPath + fileName).toPath();
		FileChannel fc = FileChannel.open(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
		fc.write(Blob);
	}

	public void regesterListener(NetworkInterface observer) {
		this.observer = observer;
	}
}
