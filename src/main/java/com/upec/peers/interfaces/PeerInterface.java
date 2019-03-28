package com.upec.peers.interfaces;

import com.upec.peers.interfaces.dialogs.ConnexionDialog;
import com.upec.peers.interfaces.dialogs.DownloadFile;
import com.upec.peers.interfaces.dialogs.SendMessageDialog;
import com.upec.peers.network.NetworkCore;
import com.upec.peers.network.objects.PeerAddress;
import com.upec.peers.network.utils.NetworkObserver;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class PeerInterface extends JFrame implements NetworkObserver {
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

    private NetworkCore networkCore;

    public PeerInterface(NetworkCore networkCore) {
        add(rootPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(800, 400));

        consolePanel.setPreferredSize(new Dimension(400, 600));
        peerListPanel.setPreferredSize(new Dimension(150, 600));
        connexionListPanel.setPreferredSize(new Dimension(150, 600));
        actionListPanel.setSize(new Dimension(100, 600));

        connectionsListModal = new DefaultListModel<>();
        knownPeersListModal = new DefaultListModel<>();
        connectionsList.setModel(connectionsListModal);
        knownPeersList.setModel(knownPeersListModal);

        console.setBackground(Color.BLACK);
        console.setForeground(Color.LIGHT_GRAY);
        console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        connectButton.addActionListener(e -> {
            var dialog = new ConnexionDialog((address, port) -> {
                networkCore.instantiateConnection(address, Integer.parseInt(port));
            });
            dialog.setVisible(true);
        });

        disconnectButton.addActionListener(e -> {
            if (! this.connectionsList.isSelectionEmpty()) {
//                this.networkCore.terminateConnection();
                JOptionPane.showMessageDialog(null, "You were disconnected", "Disconnected", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        listOfFilesButton.addActionListener(e -> {
            if (! this.connectionsList.isSelectionEmpty()) {
                console.append("new files\n");
            }
        });

        listOfPeersButton.addActionListener(e -> {
            if (! this.connectionsList.isSelectionEmpty()) {
                knownPeersListModal.addElement("new peer");
            }
        });

        downloadButton.addActionListener(e -> {
            if (! this.connectionsList.isSelectionEmpty()) {
                var dialog = new DownloadFile(fileName -> System.out.println("file: " + fileName));
                dialog.setVisible(true);
            }
        });

        sendMessageButton.addActionListener(e -> {
            if (! this.connectionsList.isSelectionEmpty()) {
                var d = new SendMessageDialog(s -> System.out.println("message: " + s));
                d.setVisible(true);
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
    public void listOfKnowPeersChanged(List<PeerAddress> peerAddresses) {
        this.knownPeersListModal.clear();
        peerAddresses.forEach(peerAddress -> knownPeersListModal.addElement(peerAddress.toString()));
    }

    @Override
    public void logInformations(String data) {
        this.console.append(data);
    }
}
