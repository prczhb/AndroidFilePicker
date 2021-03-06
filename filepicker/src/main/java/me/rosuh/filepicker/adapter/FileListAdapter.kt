package me.rosuh.filepicker.adapter

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import me.rosuh.filepicker.FilePickerActivity
import me.rosuh.filepicker.R
import me.rosuh.filepicker.bean.FileItemBeanImpl
import me.rosuh.filepicker.config.FilePickerManager
import java.io.File

/**
 *
 * @author rosu
 * @date 2018/11/21
 * 文件列表适配器类
 */
class FileListAdapter(private val activity: FilePickerActivity, var data: ArrayList<FileItemBeanImpl>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var recyclerViewListener: RecyclerViewListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            EMPTY_LIST_FILE_TYPE -> {
                EmptyListHolder(LayoutInflater.from(activity).inflate(R.layout.list_item_empty, parent, false))
            }
            else -> {
                val itemView = LayoutInflater.from(activity).inflate(R.layout.item_list_file_picker, parent, false)
                FileListItemHolder(itemView)
            }
        }
    }

    override fun getItemCount(): Int {
        return data?.size?:10
    }

    override fun getItemViewType(position: Int): Int {
        if (data == null){
            return EMPTY_LIST_FILE_TYPE
        }
        return DEFAULT_FILE_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            EMPTY_LIST_FILE_TYPE -> {
                (holder as EmptyListHolder).bind()
            }
            else -> {
                (holder as FileListItemHolder).bind(data!![position], position)
            }
        }
    }

    fun getItem(position: Int):FileItemBeanImpl?{
        if (position >= 0 &&
            position < data!!.size &&
            getItemViewType(position) == DEFAULT_FILE_TYPE
        ) return data!![position]
        return null
    }

    inner class FileListItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView){

        private val isSkipDir: Boolean = FilePickerManager.config.isSkipDir
        private val mTvFileName = itemView.findViewById<TextView>(R.id.tv_list_file_picker)!!
        private val mCbItem = itemView.findViewById<CheckBox>(R.id.cb_list_file_picker)!!
        private val mIcon = itemView.findViewById<ImageView>(R.id.iv_icon_list_file_picker)!!
        private var mItemBeanImpl:FileItemBeanImpl ?= null
        private var mPosition:Int? = null


        fun bind(itemImpl: FileItemBeanImpl, position: Int) {
            mItemBeanImpl = itemImpl
            mPosition = position

            mTvFileName.text = itemImpl.fileName
            mCbItem.isChecked = itemImpl.isChecked()
            mCbItem.visibility = View.VISIBLE

            val isDir = File(itemImpl.filePath).isDirectory

            if (isDir) {
                mIcon.setImageResource(R.drawable.ic_folder)
                mCbItem.visibility = if (isSkipDir) View.INVISIBLE else View.VISIBLE
                return
            }

            val resId: Int = itemImpl.fileType?.fileIconResId ?: R.drawable.ic_unknown
            mIcon.setImageResource(resId)
        }

    }

    inner class EmptyListHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(){
            val anim1 = ObjectAnimator.ofInt(itemView.findViewById<View>(R.id.view_empty_icon), "backgroundColor",
                Color.parseColor("#CCCCCC"), Color.parseColor("#DDDDDD"))

            val anim2 = ObjectAnimator.ofInt(itemView.findViewById<View>(R.id.view_empty_str), "backgroundColor",
                Color.parseColor("#CCCCCC"), Color.parseColor("#DDDDDD"))
            anim1.duration = 2000
            anim2.duration = 2000
            anim1.setEvaluator(ArgbEvaluator())
            anim2.setEvaluator(ArgbEvaluator())
            anim1.repeatMode = ValueAnimator.REVERSE
            anim1.repeatCount = ValueAnimator.INFINITE
            anim2.repeatMode = ValueAnimator.REVERSE
            anim2.repeatCount = ValueAnimator.INFINITE
            anim1.start()
            anim2.start()
        }
    }

    companion object {
        const val EMPTY_LIST_FILE_TYPE = 1000
        const val DEFAULT_FILE_TYPE = 10001
    }
}