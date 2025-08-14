package com.github.fligneul.debtplugin.debt

import com.intellij.icons.AllIcons
import java.awt.Component
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.AbstractCellEditor
import javax.swing.JButton
import javax.swing.JTable
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer

class DeleteButtonCell(private val onClick: (Int) -> Unit) : AbstractCellEditor(), TableCellRenderer, TableCellEditor {

    private val button = JButton(AllIcons.General.Delete)
    private var row: Int = 0

    init {
        button.isOpaque = true
        button.addActionListener(ActionListener { e ->
            fireEditingStopped()
            onClick(row)
        })
    }

    override fun getTableCellRendererComponent(table: JTable?, value: Any?, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component {
        return button
    }

    override fun getTableCellEditorComponent(table: JTable?, value: Any?, isSelected: Boolean, row: Int, column: Int): Component {
        this.row = row
        return button
    }

    override fun getCellEditorValue(): Any? {
        return null // Button doesn't have a value
    }
}
