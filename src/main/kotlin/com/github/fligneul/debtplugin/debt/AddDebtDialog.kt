package com.github.fligneul.debtplugin.debt

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.panel
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JLabel

class AddDebtDialog : DialogWrapper(true) {
    private val descriptionField = JBTextField()
    private val statusComboBox = JComboBox(DefaultComboBoxModel(Status.values()))
    private val priorityComboBox = JComboBox(DefaultComboBoxModel(Priority.values()))

    var description: String = ""
        private set
    var status: Status = Status.Submitted
        private set
    var priority: Priority = Priority.Medium
        private set

    init {
        title = "Add New Debt"
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            row(JLabel("Description:")) {
                component(descriptionField)
            }
            row(JLabel("Status:")) {
                component(statusComboBox)
            }
            row(JLabel("Priority:")) {
                component(priorityComboBox)
            }
        }
    }

    override fun doOKAction() {
        description = descriptionField.text
        status = statusComboBox.selectedItem as Status
        priority = priorityComboBox.selectedItem as Priority
        super.doOKAction()
    }
}
