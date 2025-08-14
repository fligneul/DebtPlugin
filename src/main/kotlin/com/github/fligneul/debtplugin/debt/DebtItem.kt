package com.github.fligneul.debtplugin.debt

data class DebtItem(
    val file: String,
    val line: Int,
    val description: String,
)
