package net.dankito.filechooserdialog.service

import net.dankito.filechooserdialog.model.FileChooserDialogType
import java.io.File


class SelectedFilesManager(private var dialogType: FileChooserDialogType) {

    val selectedFiles: List<File> = mutableListOf()

    private val selectedFilesChangedListeners = ArrayList<(List<File>) -> Unit>()


    fun toggleFileIsSelected(file: File) {
        if(dialogType != FileChooserDialogType.SelectMultipleFiles) {
            clearSelectedFiles()
        }

        if(selectedFiles.contains(file)) {
            (selectedFiles as MutableList).remove(file)
        }
        else {
            (selectedFiles as MutableList).add(file)
        }

        selectedFilesChangedListeners.forEach { it(selectedFiles) }
    }

    private fun clearSelectedFiles() {
        (selectedFiles as MutableList).clear()
    }


    fun isFileSelected(file: File): Boolean {
        return selectedFiles.contains(file)
    }


    fun addSelectedFilesChangedListeners(listener: (List<File>) -> Unit) {
        selectedFilesChangedListeners.add(listener)
    }

}