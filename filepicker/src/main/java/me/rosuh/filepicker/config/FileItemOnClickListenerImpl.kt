package me.rosuh.filepicker.config

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import me.rosuh.filepicker.R
import me.rosuh.filepicker.adapter.FileListAdapter
import me.rosuh.filepicker.bean.FileItemBeanImpl
import java.io.File

/**
 *
 * @author rosu
 * @date 2018/11/30
 * 提供条目点击监听器的默认实现
 */
class FileItemOnClickListenerImpl : FileItemOnClickListener {
    /**
     * 条目被点击默认实现
     */
    override fun onItemClick(itemAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, itemView: View, position: Int) {

    }

    /**
     * 条目子控件被点击默认实现
     */
    override fun onItemChildClick(itemAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, itemView: View, position: Int) {

    }

    /**
     * 条目被长按默认实现
     */
    override fun onItemLongClick(itemAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, itemView: View, position: Int) {
        if (itemView.id != R.id.item_list_file_picker) return
        val item = (itemAdapter as FileListAdapter).getItem(position)
        item ?: return
        val file = File(item.filePath)
        val isSkipDir = FilePickerConfig.getInstance(FilePickerManager.instance).isSkipDir
        // 如果是文件夹并且没有略过文件夹
        if (file.exists() && file.isDirectory && isSkipDir) return
        val cb = itemView.findViewById<CheckBox>(R.id.cb_list_file_picker)
        val isChecked = cb.isChecked
        cb.visibility = View.VISIBLE
        if (isChecked) {
            cb.isChecked = false
            item.isChecked = cb.isChecked
        } else {
            cb.isChecked = true
            item.isChecked = cb.isChecked
        }
    }
}