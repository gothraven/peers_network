package com.upec.peers.interfaces.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.BiConsumer;

public class DownloadFile extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fileNameTextField;
    private JFormattedTextField fileSizeTextField;
    private BiConsumer<String, String> onActionOk;

    public DownloadFile(BiConsumer<String, String> onActionOk) {
        this.onActionOk = onActionOk;
        setContentPane(contentPane);
        setSize(300, 200);
        setTitle("DOWNLOAD");
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        fileNameTextField.setBackground(Color.white);
        fileNameTextField.setBorder(BorderFactory.createTitledBorder("file name"));

        fileSizeTextField.setBackground(Color.white);
        fileSizeTextField.setBorder(BorderFactory.createTitledBorder("file size"));

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
        var fileName = this.fileNameTextField.getText();
        var fileSize = this.fileSizeTextField.getText();
        this.onActionOk.accept(fileName, fileSize);
        dispose();
    }

    private void onCancel() {
        dispose();
    }

}
