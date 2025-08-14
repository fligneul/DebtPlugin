package com.github.fligneul.debtplugin.debt

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class DebtToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val debtToolWindow = DebtToolWindow(project)
        val content = ContentFactory.getInstance().createContent(debtToolWindow.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }
}
