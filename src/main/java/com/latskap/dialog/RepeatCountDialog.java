package com.latskap.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.sun.istack.Nullable;

import javax.swing.*;
import java.awt.*;

public class RepeatCountDialog extends DialogWrapper {
    private JTextField repeatCount;

    public RepeatCountDialog(int x, int y) {
        this();
        setInitialLocationCallback(() -> new Point(x, y));
    }
    public RepeatCountDialog() {
        super(true);
        init();
        setTitle("Set Repeat Count");
        setResizable(false);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("repeat count:");
        label.setPreferredSize(new Dimension(100, 26));
        dialogPanel.add(label);
        JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(100, 26));
        jTextField.setToolTipText("Enter Repeat Count");
        dialogPanel.add(jTextField);
        repeatCount = jTextField;
        return dialogPanel;
    }

    @Override
    protected void doOKAction() {
        if (repeatCount.getText().isEmpty() || !repeatCount.getText().trim().chars().allMatch(Character::isDigit))
            repeatCount.setText("1");
        close(OK_EXIT_CODE);
    }

    public JTextField getRepeatCount() {
        return repeatCount;
    }
}