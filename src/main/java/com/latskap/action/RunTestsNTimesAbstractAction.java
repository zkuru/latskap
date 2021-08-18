package com.latskap.action;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.latskap.dialog.RepeatCountDialog;
import com.latskap.dialog.RepeatCountDialogFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class RunTestsNTimesAbstractAction extends AnAction {
    abstract boolean isEnabled(AnActionEvent e);
    abstract void updateConfiguration(ExecutionEnvironment env, int repeatCount);

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(isEnabled(e));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Optional<ExecutionEnvironment> executionEnvironment = getExecutionEnvironment(e);
        RepeatCountDialog repeatCountDialog = new RepeatCountDialogFactory(e).create();
        if (repeatCountDialog.showAndGet()) {
            executionEnvironment.ifPresent(env -> {
                        updateConfiguration(env, repeatCountDialog.getRepeatCountAsInt());
                        execute(env);
                    }
            );
        }
    }

    Optional<ExecutionEnvironment> getExecutionEnvironment(AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            RunContentDescriptor selectedContent = RunContentManager.getInstance(project).getSelectedContent();
            if (selectedContent != null) {
                DataContext dataContext = DataManager.getInstance().getDataContext(selectedContent.getComponent());
                return Optional.ofNullable(LangDataKeys.EXECUTION_ENVIRONMENT.getData(dataContext));
            }
        }
        return Optional.empty();
    }

    private static void execute(ExecutionEnvironment environment) {
        try {
            environment.getRunner().execute(environment);
        } catch (ExecutionException executionException) {
            Messages.showErrorDialog(environment.getProject(), "Failed to execute test configuration.", "Error");
        }
    }
}