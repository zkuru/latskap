package com.latskap.action;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.junit.JUnitConfiguration;
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
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;
import java.util.Optional;

import static com.intellij.rt.execution.junit.RepeatCount.N;

public class RunTestsNTimesAction extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(isEnabled(e));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        RepeatCountDialog repeatCountDialog = createRepeatCountDialog(e);
        repeatCountDialog.show();

        getExecutionEnvironment(e)
                .ifPresent(env -> getJUnitConfiguration(env)
                        .ifPresent(conf -> {
                            updateJUnitConfiguration(conf, repeatCountDialog);
                            execute(env);
                        })
                );
    }

    private static boolean isEnabled(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null)
            return false;
        Optional<ExecutionEnvironment> environment = getExecutionEnvironment(e);
        return environment.isPresent() && environment.get().getRunProfile() instanceof JUnitConfiguration;
    }

    private static RepeatCountDialog createRepeatCountDialog(@NotNull AnActionEvent e) {
        MouseEvent mouseEvent = (MouseEvent) e.getInputEvent();
        return new RepeatCountDialog(mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen());
    }

    private static Optional<ExecutionEnvironment> getExecutionEnvironment(AnActionEvent e) {
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

    private static Optional<JUnitConfiguration> getJUnitConfiguration(ExecutionEnvironment environment) {
        RunnerAndConfigurationSettings runnerAndConfigurationSettings = environment.getRunnerAndConfigurationSettings();
        return runnerAndConfigurationSettings != null
                ? Optional.of((JUnitConfiguration) runnerAndConfigurationSettings.getConfiguration())
                : Optional.empty();
    }

    private static void updateJUnitConfiguration(JUnitConfiguration jUnitConfiguration,
                                                 RepeatCountDialog repeatCountDialog) {
        jUnitConfiguration.setRepeatMode(N);
        jUnitConfiguration.setRepeatCount(getRepeatCountAsInt(repeatCountDialog));
    }

    private static void execute(ExecutionEnvironment environment) {
        try {
            environment.getRunner().execute(environment);
        } catch (ExecutionException executionException) {
            Messages.showErrorDialog(environment.getProject(), "Failed to execute test configuration.", "Error");
        }
    }

    private static int getRepeatCountAsInt(RepeatCountDialog repeatCountDialog) {
        return Integer.parseInt(repeatCountDialog.getRepeatCount().getText());
    }
}