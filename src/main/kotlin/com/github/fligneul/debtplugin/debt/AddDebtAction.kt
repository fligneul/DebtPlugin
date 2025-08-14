package com.github.fligneul.debtplugin.debt

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.Messages

class AddDebtAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val dialog = AddDebtDialog()
        if (dialog.showAndGet()) {
            val debtService = project.service<DebtService>()
            val debtItem = DebtItem(
                file = file.path,
                line = editor.caretModel.logicalPosition.line + 1,
                description = dialog.description,
                status = dialog.status,
                priority = dialog.priority
            )
            debtService.add(debtItem)
        }
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = e.project != null && editor != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
