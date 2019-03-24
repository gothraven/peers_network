package com.upec.peers.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test extends JFrame {
    private JPanel panelRoot;
    private JPanel panelConsole;
    private JPanel panelOfListPeers;
    private JPanel panelOfListConnexions;
    private JPanel panelOfListActions;
    private JButton connect;
    private JButton disconnect;
    private JButton sendMessage;
    private JButton listOfPeersRequest;
    private JButton listOfFilesRequest;
    private JButton downloadFile;
    private JList listConnexions;
    private JList listPeers;
    private JTextArea console;

    public Test() {
        add(panelRoot);
        console.setBackground(Color.BLACK);
        console.setForeground(Color.LIGHT_GRAY);
        console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        console.append("hey its me\n");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        connect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                var d = new ConnexionDialog();
                d.show();
                //d.getAdress();//String
                //d.getPort();//int
            }
        });
        disconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new JOptionPane().showMessageDialog(null, "You are disconnected", "Disconnecting ...", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        sendMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                var d = new SendMessageDialog();
                d.show();

            }
        });
        listOfPeersRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // affiche list sur la console puis sur listOfPeers
                console.append("affiche list sur la console puis sur listOfPeers\n");
            }
        });
        listOfFilesRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //  new CreateDialogFromOptionPane (4);
                console.append("affiche list file  sur la console puis rien\n");
            }
        });
        downloadFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // new CreateDialogFromOptionPane (5);
                var d = new DownloadFile();
                d.show();
                //d.getFileName() ;
                //d.getBegin();
                //d.getEnd();

            }
        });
    }
}
