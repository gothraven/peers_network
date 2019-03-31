package com.upec.peers.interfaces;

import com.upec.peers.interfaces.dialogs.ConnexionDialog;
import com.upec.peers.interfaces.dialogs.DownloadFile;
import com.upec.peers.interfaces.dialogs.SendMessageDialog;
import com.upec.peers.network.NetworkCore;
import com.upec.peers.network.objects.PeerAddress;
import com.upec.peers.network.utils.NetworkObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class NetworkInterface extends JFrame implements NetworkObserver {
    private JPanel rootPanel;
    private JPanel consolePanel;
    private JPanel peerListPanel;
    private JPanel connexionListPanel;
    private JPanel actionListPanel;
    private DefaultListModel<String> connectionsListModal;
    private DefaultListModel<String> knownPeersListModal;
    private JList<String> connectionsList;
    private JList<String> knownPeersList;
    private JButton connectButton;
    private JButton disconnectButton;
    private JButton listOfFilesButton;
    private JButton listOfPeersButton;
    private JButton downloadButton;
    private JButton sendMessageButton;
    private JTextArea console;
    private JButton sendListeningPortButton;
    private JButton clearButton;

    private NetworkCore networkCore;

    private Handler logHandler = new Handler() {
        @Override
        public void publish(LogRecord record) {
            console.append(record.getMessage() + "\n");
        }

        @Override
        public void flush() {}

        @Override
        public void close() throws SecurityException {}
    };

    public NetworkInterface(NetworkCore networkCore) {
        this.networkCore = networkCore;
        add(rootPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(1200, 600));

        consolePanel.setPreferredSize(new Dimension(400, 600));
        peerListPanel.setPreferredSize(new Dimension(150, 600));
        connexionListPanel.setPreferredSize(new Dimension(150, 600));
        actionListPanel.setSize(new Dimension(100, 600));

        connectionsListModal = new DefaultListModel<>();
        knownPeersListModal = new DefaultListModel<>();
        connectionsList.setModel(connectionsListModal);
        knownPeersList.setModel(knownPeersListModal);

        rootPanel.setBorder(BorderFactory.createTitledBorder("Peers Network Interface"));
        actionListPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        consolePanel.setBorder(BorderFactory.createTitledBorder("Client Console"));
        connectionsList.setBorder(BorderFactory.createTitledBorder("Connections List"));
        knownPeersList.setBorder(BorderFactory.createTitledBorder("Known Peers List"));

        console.setBackground(Color.BLACK);
        console.setForeground(Color.LIGHT_GRAY);
        console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        connectButton.addActionListener(e -> {
            var dialog = new ConnexionDialog((address, port) ->
                    this.networkCore.instantiateConnection(address, Integer.parseInt(port)));
            dialog.setVisible(true);
        });

        disconnectButton.addActionListener(e -> {
            if (! this.connectionsList.isSelectionEmpty()) {
                var identifier = this.connectionsListModal.get(this.connectionsList.getSelectedIndex());
                this.networkCore.terminateConnection(identifier);
            }
        });

        listOfFilesButton.addActionListener(e -> {
            if (! this.connectionsList.isSelectionEmpty()) {
                var identifier = this.connectionsListModal.get(this.connectionsList.getSelectedIndex());
                this.networkCore.askForListOfSharedFiles(identifier);
            }
        });

        listOfPeersButton.addActionListener(e -> {
            if (! this.connectionsList.isSelectionEmpty()) {
                var identifier = this.connectionsListModal.get(this.connectionsList.getSelectedIndex());
                this.networkCore.askForListOfPeers(identifier);
            }
        });

        downloadButton.addActionListener(e -> {
            if (! this.connectionsList.isSelectionEmpty()) {
	            var dialog = new DownloadFile((fileName, fileSize) -> {
		            var identifier = this.connectionsListModal.get(this.connectionsList.getSelectedIndex());
		            this.networkCore.downloadAFile(identifier, fileName, Long.parseLong(fileSize));
	            });
                dialog.setVisible(true);
            }
        });

        sendMessageButton.addActionListener(e -> {
            if (! this.connectionsList.isSelectionEmpty()) {
                var d = new SendMessageDialog(message -> {
                    var identifier = this.connectionsListModal.get(this.connectionsList.getSelectedIndex());
                    this.networkCore.sendMessage(identifier, message);
                });
                d.setVisible(true);
            }
        });

        sendListeningPortButton.addActionListener(e -> {
            if (! this.connectionsList.isSelectionEmpty()) {
                var identifier = this.connectionsListModal.get(this.connectionsList.getSelectedIndex());
                this.networkCore.sendListeningPort(identifier);
            }
        });

        clearButton.addActionListener(e -> {
            this.console.setText("");
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                networkCore.shutDown();
            }
        });

        setVisible(true);
    }

    @Override
    public void listOfConnectionsChanged(Collection<String> connections) {
        this.connectionsListModal.clear();
        connections.forEach(connection -> connectionsListModal.addElement(connection));
    }

    @Override
    public void listOfKnowPeersChanged(Collection<PeerAddress> peerAddresses) {
        this.knownPeersListModal.clear();
        peerAddresses.forEach(peerAddress -> knownPeersListModal.addElement(peerAddress.toString()));
    }

    @Override
    public void logInformations(String data) {
        this.console.append(data);
    }

    public Handler getLogHandler() {
        return logHandler;
    }
}
