package com.github.fligneul.debtplugin.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent

class DebtSettingsConfigurable(private val project: Project) : Configurable {

    private val settings = project.service<DebtSettings>()
    private val debtFilePathField = JBTextField()

    override fun getDisplayName(): String {
        return "Debt Plugin"
    }

    override fun createComponent(): JComponent {
        debtFilePathField.text = settings.state.debtFilePath
        return FormBuilder.createFormBuilder()
            .addLabeledComponent("Debt file path:", debtFilePathField)
            .panel
    }

    override fun isModified(): Boolean {
        return debtFilePathField.text != settings.state.debtFilePath
    }

    override fun apply() {
        settings.state.debtFilePath = debtFilePathField.text
        project.messageBus.syncPublisher(DebtSettings.TOPIC).settingsChanged(settings.state)
    }

    override fun reset() {
        debtFilePathField.text = settings.state.debtFilePath
    }
}
