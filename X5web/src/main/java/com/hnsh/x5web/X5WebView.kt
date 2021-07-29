package com.hnsh.x5web

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

/**
 * @Description:
 * @Author:   Hsp
 * @Email:    1101121039@qq.com
 * @CreateTime:     2021/7/29 15:52
 * @UpdateRemark:   更新说明：
 */
class X5WebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {
    
    init {
        this.setBackgroundColor(
            ResourcesCompat.getColor(
                context.resources,
                R.color.web_gray,
                context.theme
            )
        )
        this.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
                p1?.let {
                    p0?.loadUrl(it)
                }
                return true
            }
        }
        initWebViewSettings()
        this.view.isClickable = true
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebViewSettings() {
        val webSettings = this.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.allowFileAccess = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.useWideViewPort = true
        webSettings.setSupportMultipleWindows(true)
        webSettings.setAppCacheEnabled(true)
        webSettings.domStorageEnabled = true
        webSettings.setGeolocationEnabled(true)
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE)
        webSettings.pluginState = WebSettings.PluginState.ON_DEMAND
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
    }
}