package com.github.fligneul.debtplugin.debt

import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseButton
import javafx.scene.layout.BorderPane
import javax.swing.JComponent

class DebtToolWindow(private val project: Project) {

    private val debtService = project.service<DebtService>()
    private val tableView = TableView<DebtItem>()

    fun getContent(): JComponent {
        val jfxPanel = JFXPanel()
        Platform.runLater {
            val root = BorderPane()
            val scene = Scene(root, 800.0, 600.0)
            jfxPanel.scene = scene

            val fileColumn = TableColumn<DebtItem, String>("File")
            fileColumn.cellValueFactory = PropertyValueFactory("file")

            val lineColumn = TableColumn<DebtItem, Int>("Line")
            lineColumn.cellValueFactory = PropertyValueFactory("line")

            val descriptionColumn = TableColumn<DebtItem, String>("Description")
            descriptionColumn.cellValueFactory = PropertyValueFactory("description")

            tableView.columns.addAll(fileColumn, lineColumn, descriptionColumn)
            root.center = tableView

            tableView.setOnMouseClicked { event ->
                if (event.button == MouseButton.PRIMARY && event.clickCount == 2) {
                    val debtItem = tableView.selectionModel.selectedItem
                    if (debtItem != null) {
                        val virtualFile = LocalFileSystem.getInstance().findFileByPath(debtItem.file)
                        if (virtualFile != null) {
                            val descriptor = OpenFileDescriptor(project, virtualFile, debtItem.line - 1, 0)
                            FileEditorManager.getInstance(project).openTextEditor(descriptor, true)
                        }
                    }
                }
            }

            val refreshButton = Button("Refresh")
            refreshButton.setOnAction {
                updateTable()
            }
            root.bottom = refreshButton

            updateTable()
        }
        return jfxPanel
    }

    private fun updateTable() {
        val debtItems = FXCollections.observableArrayList(debtService.all())
        tableView.items = debtItems
    }
}
