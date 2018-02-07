package net.dankito.filechooserdialog.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import net.dankito.filechooserdialog.service.FilesService
import net.dankito.filechooserdialog.ui.adapter.DirectoryContentAdapter
import java.io.File


class DirectoryContentView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {


    var currentDirectoryChangedListener: ((currentDirectory: File) -> Unit)? = null

    var selectedFilesChangedListener: ((List<File>) -> Unit)? = null

    val selectedFiles: List<File> = mutableListOf()


    private val contentAdapter = DirectoryContentAdapter(selectedFiles)

    private val fileService = FilesService()


    init {
        layoutManager = LinearLayoutManager(context)
        isLongClickable = true

        this.adapter = contentAdapter
        contentAdapter.itemClickListener = { file -> fileClicked(file) }
    }


    fun setCurrentDirectory(directory: File) {
        if(showContentForDirectory(directory)) {
            currentDirectoryChangedListener?.invoke(directory)
        }
    }

    private fun showContentForDirectory(directory: File): Boolean {
        fileService.getFilesOfDirectorySorted(directory)?.let { files ->
            contentAdapter.items = files

            return true
        }

        return false
    }

    private fun fileClicked(file: File) {
        if(file.isDirectory) {
            setCurrentDirectory(file)
        }
        else {
            toggleFileIsSelected(file)
        }
    }

    private fun toggleFileIsSelected(file: File) {
        if(selectedFiles.contains(file)) {
            (selectedFiles as MutableList).remove(file)
        }
        else {
            (selectedFiles as MutableList).add(file)
        }

        adapter.notifyDataSetChanged()

        selectedFilesChangedListener?.invoke(selectedFiles)
    }

}