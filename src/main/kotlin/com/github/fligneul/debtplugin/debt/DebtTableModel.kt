package com.github.fligneul.debtplugin.debt

import javax.swing.table.DefaultTableModel

class DebtTableModel(private val debtService: DebtService) : DefaultTableModel() {

    private val debtItems = mutableListOf<DebtItem>() // Internal list to store DebtItem objects

    init {
        addColumn("File")
        addColumn("Line")
        addColumn("Description")
        addColumn("Status")
        addColumn("Priority")
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        // Make the first two columns (File and Line) non-editable
        return column >= 2
    }

    override fun getColumnClass(columnIndex: Int): Class<*> {
        return when (columnIndex) {
            3 -> Status::class.java // Status column
            4 -> Priority::class.java // Priority column
            else -> super.getColumnClass(columnIndex)
        }
    }

    override fun setValueAt(aValue: Any?, row: Int, column: Int) {
        val oldDebtItem = debtItems[row]
        val updatedDebtItem = when (column) {
            2 -> oldDebtItem.copy(description = aValue as String)
            3 -> oldDebtItem.copy(status = aValue as Status)
            4 -> oldDebtItem.copy(priority = aValue as Priority)
            else -> oldDebtItem
        }

        if (updatedDebtItem != oldDebtItem) {
            debtItems[row] = updatedDebtItem
            debtService.update(oldDebtItem, updatedDebtItem)
            super.setValueAt(aValue, row, column)
        }
    }

    // Method to add DebtItem directly
    fun addDebtItem(debtItem: DebtItem) {
        debtItems.add(debtItem) // Add to internal list
        addRow(arrayOf(debtItem.file, debtItem.line, debtItem.description, debtItem.status, debtItem.priority)) // Add only visible data to DefaultTableModel
    }

    // Method to clear all debt items
    fun clearAll() {
        debtItems.clear()
        rowCount = 0 // Clear rows in DefaultTableModel
    }
}