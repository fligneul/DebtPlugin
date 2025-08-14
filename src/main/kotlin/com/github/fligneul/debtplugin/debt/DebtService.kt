package com.github.fligneul.debtplugin.debt

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import java.io.File

@Service(Service.Level.PROJECT)
class DebtService(private val project: Project) {

    private val gson = Gson()
    private val debtFile = File(project.basePath, "debt.json")
    private val debts = mutableListOf<DebtItem>()

    init {
        loadDebts()
    }

    fun add(debtItem: DebtItem) {
        debts.add(debtItem)
        saveDebts()
    }

    fun remove(debtItem: DebtItem) {
        debts.remove(debtItem)
        saveDebts()
    }

    fun all(): List<DebtItem> {
        return debts.toList()
    }

    private fun loadDebts() {
        if (debtFile.exists()) {
            val type = object : TypeToken<List<DebtItem>>() {}.type
            debts.clear()
            debts.addAll(gson.fromJson(debtFile.readText(), type))
        }
    }

    private fun saveDebts() {
        debtFile.writeText(gson.toJson(debts))
    }
}
