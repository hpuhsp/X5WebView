package com.smart.web

import android.app.Application
import android.util.Log
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener

/**
 * @Description:
 * @Author:   Hsp
 * @Email:    1101121039@qq.com
 * @CreateTime:     2021/7/29 15:46
 * @UpdateRemark:   更新说明：
 */
class MyApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        initX5Config()
    }
    
    private fun initX5Config() {
        // TBS内核首次使用和加载时，ART虚拟机会将Dex文件转为Oat，该过程由系统底层触发且耗时较长，很容易引起anr问题，解决方法是使用TBS的 ”dex2oat优化方案“。
        // 此处设置开启优化方案
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        // 设备无WIFI连接情况下依然下载
        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.setTbsListener(object : TbsListener {
            override fun onDownloadFinish(p0: Int) {
                Log.d("X5", "onDownloadFinish --------------------->下载X5内核完成：$p0");
            }
            
            override fun onInstallFinish(p0: Int) {
                Log.d("X5", "onInstallFinish --------------------->安装X5内核进度：$p0");
            }
            
            override fun onDownloadProgress(p0: Int) {
                Log.d("X5", "onDownloadProgress --------------------->下载X5内核进度：$p0");
            }
        })
        // 初始化环境
        QbSdk.initX5Environment(this, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
            }
            
            override fun onViewInitFinished(p0: Boolean) {
            }
        })
    }
}