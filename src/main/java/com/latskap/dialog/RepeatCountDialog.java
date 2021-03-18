package com.latskap.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.VerticalFlowLayout;
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
        JPanel dialogPanel = new JPanel(new VerticalFlowLayout());
        JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(10, 28));
        jTextField.setToolTipText("Enter Repeat Count");
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