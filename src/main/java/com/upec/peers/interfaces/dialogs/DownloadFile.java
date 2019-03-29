package com.upec.peers.interfaces.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class DownloadFile extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fileName;
    private Consumer<String> onActionOk;

    public DownloadFile(Consumer<String> onActionOk) {
        this.onActionOk = onActionOk;
        setContentPane(contentPane);
        setSize(300, 120);
        setTitle("DOWNLOAD");
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        fileName.setBackground(Color.white);
        fileName.setBorder(BorderFactory.createTitledBorder("SharedFile name"));

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
        var fileName = this.fileName.getText();
        this.onActionOk.accept(fileName);
        dispose();
    }

    private void onCancel() {
        fileName.replaceSelection("");
        dispose();
    }
}
