package com.latskap.dialog;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class RepeatCountDialogFactory {
    private final InputEvent inputEvent;
    public RepeatCountDialogFactory(@NotNull AnActionEvent e) {
        this.inputEvent = e.getInputEvent();
    }

    public RepeatCountDialog create() {
        if (inputEvent instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) inputEvent;
            return new RepeatCountDialog(mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen());
        } else if (inputEvent instanceof KeyEvent)
            return new RepeatCountDialog();
        throw new IllegalStateException("Repeat count dialog for current test configureation can not be created.");
    }
}