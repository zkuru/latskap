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
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.intellij.rt.execution.junit.RepeatCount.N;

public class RunTestsNTimes extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(isEnabled(e));
    }

    private static boolean isEnabled(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null)
            return false;
        ExecutionEnvironment environment = LangDataKeys.EXECUTION_ENVIRONMENT.getData(DataManager.getInstance().getDataContext(RunContentManager.getInstance(project).getSelectedContent().getComponent()));
        if (environment == null)
            return false;
        return environment.getRunProfile() instanceof JUnitConfiguration;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        RepeatTestsDialog repeatTestsDialog = new RepeatTestsDialog();
        repeatTestsDialog.show();

        Optional<ExecutionEnvironment> environment = getExecutionEnvironment(e);
        environment.ifPresent(executionEnvironment -> executeIfRunnerSettingsArePresent(executionEnvironment, repeatTestsDialog));
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

    private static void executeIfRunnerSettingsArePresent(ExecutionEnvironment environment,
                                                          RepeatTestsDialog repeatTestsDialog) {
        RunnerAndConfigurationSettings runnerAndConfigurationSettings = environment.getRunnerAndConfigurationSettings();
        if (runnerAndConfigurationSettings != null) {
            updateJUnitConfiguration(runnerAndConfigurationSettings, repeatTestsDialog);
            execute(environment);
        }
    }

    private static void updateJUnitConfiguration(RunnerAndConfigurationSettings runnerAndConfigurationSettings,
                                                 RepeatTestsDialog repeatTestsDialog) {
        JUnitConfiguration jUnitConfiguration = (JUnitConfiguration) runnerAndConfigurationSettings.getConfiguration();
        jUnitConfiguration.setRepeatMode(N);
        jUnitConfiguration.setRepeatCount(getRepeatCountAsInt(repeatTestsDialog));
    }

    private static void execute(ExecutionEnvironment environment) {
        try {
            environment.getRunner().execute(environment);
        } catch (ExecutionException executionException) {
            Messages.showErrorDialog(environment.getProject(), "Failed to execute test configuration.", "Error");
        }
    }

    private static int getRepeatCountAsInt(RepeatTestsDialog repeatTestsDialog) {
        return Integer.parseInt(repeatTestsDialog.getRepeatCount().getText());
    }
}