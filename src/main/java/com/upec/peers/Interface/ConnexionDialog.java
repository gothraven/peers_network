package com.upec.peers.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConnexionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField port;
    private JTextField adress;

    public ConnexionDialog() {
        setTitle("NEW CONNEXION");
        setSize(250, 200);
        setContentPane(contentPane);
        adress.setBackground(Color.white);
        adress.setBorder(BorderFactory.createTitledBorder("Address"));

        port.setBackground(Color.white);
        port.setBorder(BorderFactory.createTitledBorder("PortCommand"));

        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static void main(String[] args) {
        ConnexionDialog dialog = new ConnexionDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public int getPort() {
        return Integer.parseInt(port.getText());
    }

    public String getAdress() {
        return adress.getText();
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
