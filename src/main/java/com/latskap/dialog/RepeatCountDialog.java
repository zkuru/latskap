package com.latskap.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.sun.istack.Nullable;

import javax.swing.*;
import java.awt.*;

public class RepeatCountDialog extends DialogWrapper {
    private JTextField repeatCount;

    public RepeatCountDialog(int x, int y) {
        super(true);
        init();
        setTitle("Set Repeat Count");
        setInitialLocationCallback(() -> new Point(x, y));
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Repeat count:");
        label.setPreferredSize(new Dimension(15, 20));
        dialogPanel.add(label, BorderLayout.CENTER);
        JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(20, 28));
        dialogPanel.add(jTextField, BorderLayout.PAGE_END);
        repeatCount = jTextField;
        return dialogPanel;
    }

    @Override
    protected void doOKAction() {
        if (!repeatCount.getText().trim().chars().allMatch(Character::isDigit))
            repeatCount.setText("1");
        close(OK_EXIT_CODE);
    }

    public JTextField getRepeatCount() {
        return repeatCount;
    }
}