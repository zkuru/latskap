package com.latskap.lineMarker;

import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.testIntegration.TestFailedLineManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RepeatTestsNTimesLineMarkerProvider extends RunLineMarkerContributor {
    @Override
    public @Nullable Info getInfo(@NotNull PsiElement e) {
        if (e instanceof PsiIdentifier) {
            PsiElement element = e.getParent();
            if (element instanceof PsiMethod) {
                AnAction myAction = ActionManager.getInstance().getAction("com.latskap.action.RunTestsNTimesAction");
                TestFailedLineManager.TestInfo testInfo = TestFailedLineManager.getInstance(e.getProject()).getTestInfo((PsiMethod) e.getParent());
                return new Info(getTestStateIcon(testInfo.myRecord, false), null, myAction);
            }
        }
        return null;
    }
}