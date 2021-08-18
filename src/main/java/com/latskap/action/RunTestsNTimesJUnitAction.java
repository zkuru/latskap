package com.latskap.action;

import com.intellij.execution.junit.JUnitConfiguration;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import java.util.Optional;

import static com.intellij.rt.execution.junit.RepeatCount.N;

public class RunTestsNTimesJUnitAction extends RunTestsNTimesAbstractAction {
    @Override
    public boolean isEnabled(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null)
            return false;
        Optional<ExecutionEnvironment> environment = getExecutionEnvironment(e);
        return environment.isPresent() && environment.get().getRunProfile() instanceof JUnitConfiguration;
    }

    @Override
    public void updateConfiguration(ExecutionEnvironment env, int repeatCount) {
        JUnitConfiguration configuration = (JUnitConfiguration) env.getRunProfile();
        configuration.setRepeatMode(N);
        configuration.setRepeatCount(repeatCount);
    }
}