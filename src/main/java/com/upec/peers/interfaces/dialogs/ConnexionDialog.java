package com.upec.peers.interfaces.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.BiConsumer;

public class ConnexionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField port;
    private JTextField address;
    private BiConsumer<String, String> onActionOk;

    public ConnexionDialog(BiConsumer<String, String> onActionOk) {
        this.onActionOk = onActionOk;
        setTitle("NEW CONNEXION");
        setSize(250, 200);
        setContentPane(contentPane);

        address.setBackground(Color.white);
        address.setBorder(BorderFactory.createTitledBorder("Address"));
        port.setBackground(Color.white);
        port.setBorder(BorderFactory.createTitledBorder("Port"));

        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e ->
                        onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        var address = this.address.getText();
        var port = this.port.getText();
        this.onActionOk.accept(address, port);
        dispose();
    }

    private void onCancel() {
        this.address.setText("");
        this.port.setText("");
        dispose();
    }
}
