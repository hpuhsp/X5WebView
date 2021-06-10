package com.hnsh.x5web

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.File
import java.security.MessageDigest


/**
 * @Description:
 * @Author:   Hsp
 * @Email:    1101121039@qq.com
 * @CreateTime:     2020/9/29 9:20
 * @UpdateRemark:   更新说明：
 */
object FileTools {

    private const val EXTERNAL_STORAGE_PERMISSION =
        "android.permission.WRITE_EXTERNAL_STORAGE"

    /**
     * 需要获取动态权限
     */
    fun getAppSdCardDir(
        context: Context,
        cacheDir: String
    ): File? {
        var appCacheDir: File? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
            && hasExternalStoragePermission(context)
        ) {
            appCacheDir =
                File(
                    ContextCompat.getExternalFilesDirs(
                        context, null
                    )[0].absolutePath, cacheDir
                )
        }
        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.cacheDir
        }
        return appCacheDir
    }

    /**
     * 固定文件名(DIRECTORY_DOWNLOADS)常量，沙盒目录
     */
    fun getDownLoadDir(
        context: Context,
        cacheDir: String
    ): File? {
        var appCacheDir: File? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
            && hasExternalStoragePermission(context)
        ) {
            appCacheDir = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath,
                cacheDir
            )
        }
        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.cacheDir
        }
        return appCacheDir
    }


    /**
     * 判断权限
     */
    private fun hasExternalStoragePermission(context: Context): Boolean {
        val perm =
            context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION)
        return perm == PackageManager.PERMISSION_GRANTED
    }


    /**
     * 根据给定存储路径获取缓存文件，当前适用于TBS Reader
     */
    fun getCustomCacheFile(context: Context, cacheDir: String, url: String): File {
        return File(getDownLoadDir(context, cacheDir), getFileName(url))
    }

    /***
     * 根据链接获取文件名（带类型的），具有唯一性
     * @param url
     * @return
     */
    fun getFileName(url: String): String {
        val fileName: String = hashKey(url).toString() + "." + getFileType(url)
        Log.e("tag", "fileName=$fileName")
        return fileName
    }

    /***
     * 获取文件类型
     * @param paramString
     * @return
     */
    fun getFileType(paramString: String): String {
        var str = ""
        if (paramString.isNullOrEmpty()) {
            Log.e("tag", "paramString---->null")
            return str
        }
        Log.e("tag", "paramString:$paramString")
        val i = paramString.lastIndexOf('.')
        if (i <= -1) {
            Log.e("tag", "i <= -1")
            return str
        }
        str = paramString.substring(i + 1)
        Log.e("tag", "paramString.substring(i + 1)------>$str")
        return str
    }

    /**
     *========================================MD5编码处理===========================================>
     */
    private fun bytesToHexString(bytes: ByteArray): String? {
        val sb = StringBuilder()
        for (i in bytes.indices) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }

    private fun hashKey(key: String): String? {
        val hashKey: String?
        hashKey = try {
            val mDigest: MessageDigest = MessageDigest.getInstance("MD5")
            mDigest.update(key.toByteArray())
            bytesToHexString(mDigest.digest())
        } catch (e: Exception) {
            key.hashCode().toString()
        }
        return hashKey
    }
}