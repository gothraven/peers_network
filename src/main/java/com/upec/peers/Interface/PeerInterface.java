package com.upec.peers.Interface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PeerInterface extends JFrame{
    private JPanel rootPanel;
    private JPanel consolePanel;
    private JPanel peerListPanel;
    private JPanel connexionListPanel;
    private JPanel actionListPanel;
    private JButton connect;
    private JButton disconnect;
    private JButton LISTOFFILESButton;
    private JButton LISTOFPEERSButton;
    private JButton DOWNLOADButton;
    private JList listOfPeers;
    private JList listOfActions;
    private JTextArea console;
    private JButton SENDMESSAGEButton;

    public PeerInterface() {

        add(rootPanel) ;
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var d = new ConnexionDialog() ;
                d.show();
            }
        });
        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


               new JOptionPane().showMessageDialog(null, "DISCONNECt", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        LISTOFFILESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JOptionPane().showMessageDialog(null, "List Of Files", "Information", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        LISTOFPEERSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JOptionPane().showMessageDialog(null, "List Of Peers ", "Information", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        DOWNLOADButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var d = new DownloadFile() ;
                d.show();
            }
        });
        SENDMESSAGEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var d = new SendMessageDialog() ;
                d.show();
            }
        });
    }
}
