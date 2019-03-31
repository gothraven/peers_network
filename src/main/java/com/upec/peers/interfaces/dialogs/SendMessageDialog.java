package com.upec.peers.interfaces.dialogs;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class SendMessageDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea messageTextArea;
    private Consumer<String> onActionOk;

    public SendMessageDialog(Consumer<String> onActionOk) {
        this.onActionOk = onActionOk;
        setContentPane(contentPane);
        setModal(true);
        setTitle("SEND MESSAGE");
        setSize(300, 300);
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
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        var message = this.messageTextArea.getText();
        this.onActionOk.accept(message);
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
