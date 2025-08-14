package com.github.fligneul.debtplugin.debt

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.control.TextInputDialog
import java.util.concurrent.CompletableFuture

class AddDebtAction : AnAction() {
    init {
        // Initializes the JavaFX toolkit
        JFXPanel()
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        val future = CompletableFuture<String>()
        Platform.runLater {
            val dialog = TextInputDialog()
            dialog.title = "New Debt"
            dialog.headerText = "Enter debt description:"
            dialog.showAndWait().ifPresentOrElse({
                future.complete(it)
            }, {
                future.cancel(false)
            })
        }

        val description = try {
            future.get()
        } catch (ex: Exception) {
            return
        }

        val debtService = project.service<DebtService>()
        val debtItem = DebtItem(
            file = file.path,
            line = editor.caretModel.logicalPosition.line + 1,
            description = description
        )
        debtService.add(debtItem)
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = e.project != null && editor != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
