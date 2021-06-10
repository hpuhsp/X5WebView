package com.hnsh.x5web

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.tencent.smtt.sdk.TbsReaderView
import java.io.File


/**
 * @Description:
 * @Author:   Hsp
 * @Email:    1101121039@qq.com
 * @CreateTime:     2020/9/28 19:13
 * @UpdateRemark:   更新说明：
 */
class FileReaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), TbsReaderView.ReaderCallback {

    companion object {
        private const val TAG = "SuperFileView"
        const val CACHE_DIR = "TbsReaderTemp"
    }

    private var mTbsReaderView: TbsReaderView? = null
    private var mOnGetFilePathListener: OnGetFilePathListener? = null


    fun setOnGetFilePathListener(mOnGetFilePathListener: OnGetFilePathListener?) {
        this.mOnGetFilePathListener = mOnGetFilePathListener
    }

    init {
        mTbsReaderView = TbsReaderView(context, this)
        this.addView(
            mTbsReaderView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

    }

    override fun onCallBackAction(integer: Int, o: Any?, o1: Any?) {
    }

    /***
     * 获取File路径
     */
    interface OnGetFilePathListener {
        fun onGetFilePath(mSuperFileView: FileReaderView?)
    }

    /**
     * 展示
     */
    fun show() {
        if (mOnGetFilePathListener != null) {
            mOnGetFilePathListener!!.onGetFilePath(this)
        }
    }

    /**
     * 显示文件的界面，退出界面以后需要销毁，否则再次加载文件无法加载成功，会一直显示加载文件进度条。
     */
    fun onStopDisplay() {
        if (mTbsReaderView != null) {
            mTbsReaderView?.onStop()
        }
    }

    /**
     * 展示文件
     */
    @SuppressLint("ShowToast")
    fun displayFile(file: File?) {
        if (null != file) {
            val localBundle = Bundle()
            localBundle.putString(TbsReaderView.KEY_FILE_PATH, file.toString())  // 文件名
            localBundle.putString(
                TbsReaderView.KEY_TEMP_PATH,
                FileTools.getDownLoadDir(context, "tbsCache")?.absolutePath  // 文件保存路径
            )
            if (mTbsReaderView == null) {
                mTbsReaderView = TbsReaderView(context, this)
            }

            Log.d(
                TAG,
                "displayFile: -------------------filePath------->" + localBundle.getString("filePath")
            )

            Log.d(
                TAG,
                "displayFile: -------------------tempPath------->" + localBundle.getString("tempPath")
            )

            val bool = mTbsReaderView?.preOpen(getFileType(file.toString()), false) ?: false
            if (bool) {
                mTbsReaderView?.openFile(localBundle)
            } else {
                Toast.makeText(context, "文件错误，打开文件失败!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e(TAG, "文件路径无效！")
        }
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private fun getFileType(paramString: String): String? {
        var str = ""
        if (TextUtils.isEmpty(paramString)) {
            return str
        }
        val i = paramString.lastIndexOf('.')
        if (i <= -1) {
            Log.e(TAG, "i <= -1")
            return str
        }
        str = paramString.substring(i + 1)
        return str
    }
}
