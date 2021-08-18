package com.latskap.action;

import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.util.PathUtil;
import com.theoryinpractice.testng.configuration.TestNGConfiguration;
import com.theoryinpractice.testng.model.TestData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;

public class RunTestsNTimesTestNgAction extends RunTestsNTimesAbstractAction {
    private static final String JAVA_AGENT_JAR_PATH = "annotation-changer-1.0-SNAPSHOT-jar-with-dependencies.jar";

    @Override
    public boolean isEnabled(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null)
            return false;
        Optional<ExecutionEnvironment> environment = getExecutionEnvironment(e);
        if (environment.isPresent()) {
            RunProfile runProfile = environment.get().getRunProfile();
            return runProfile instanceof TestNGConfiguration
                    && !((TestNGConfiguration) runProfile).getPersistantData().getMainClassName().isEmpty();
        }
        return false;
    }

    @Override
    public void updateConfiguration(ExecutionEnvironment env, int invocationCount) {
        TestNGConfiguration configuration = (TestNGConfiguration) env.getRunProfile();
        String agentOptions = getJavaAgentOptions(configuration, invocationCount);
        configuration.setVMParameters("-ea " + "-javaagent:" + getAgentJarPath() + agentOptions);
    }

    @NotNull
    private String getJavaAgentOptions(TestNGConfiguration configuration, int invocationCount) {
        TestData testData = configuration.getPersistantData();
        String className = testData.getMainClassName();
        String methodName = testData.getMethodName();
        return "=class=" + className + ",method=" + methodName + ",count=" + invocationCount;
    }

    private static String getAgentJarPath() {
        File pluginJar = new File(PathUtil.getJarPathForClass(RunTestsNTimesTestNgAction.class));
        File pluginDir = pluginJar.getParentFile();
        return "\"" + pluginDir.getPath() + "\"" + File.separator + JAVA_AGENT_JAR_PATH;
    }
}