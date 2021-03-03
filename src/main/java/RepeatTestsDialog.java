import com.intellij.openapi.ui.DialogWrapper;
import com.sun.istack.Nullable;

import javax.swing.*;
import java.awt.*;

public class RepeatTestsDialog extends DialogWrapper {
    private JTextField repeatCount;

    public RepeatTestsDialog() {
        super(true);
        init();
        setTitle("Run Tests N Times");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Repeat count:");
        label.setPreferredSize(new Dimension(30, 30));
        dialogPanel.add(label, BorderLayout.CENTER);
        JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(20, 30));
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