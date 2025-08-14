package com.github.fligneul.debtplugin.debt

import javax.swing.table.DefaultTableModel

class DebtTableModel(private val debtService: DebtService) : DefaultTableModel() {

    private val debtItems = mutableListOf<DebtItem>() // Internal list to store DebtItem objects

    init {
        addColumn("File")
        addColumn("Line")
        addColumn("Description")
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        // Make the first two columns (File and Line) non-editable
        return column >= 2
    }

    override fun setValueAt(aValue: Any?, row: Int, column: Int) {
        if (column == 2 && aValue is String) { // Only update description column
            val oldDebtItem = debtItems[row] // Get the original DebtItem from our internal list
            val updatedDebtItem = oldDebtItem.copy(description = aValue)
            debtItems[row] = updatedDebtItem // Update internal list
            debtService.update(oldDebtItem, updatedDebtItem) // Update in service
            super.setValueAt(aValue, row, column) // Update table display
        }
    }

    // Method to add DebtItem directly
    fun addDebtItem(debtItem: DebtItem) {
        debtItems.add(debtItem) // Add to internal list
        addRow(arrayOf(debtItem.file, debtItem.line, debtItem.description)) // Add only visible data to DefaultTableModel
    }

    // Method to clear all debt items
    fun clearAll() {
        debtItems.clear()
        rowCount = 0 // Clear rows in DefaultTableModel
    }
}