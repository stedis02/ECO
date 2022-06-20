package com.example.tpueco.presentation.VM

import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.tpueco.App

class StartBrowserViewModel : ViewModel() {

    var redirectUrl: String = "https://eco.tpu.app"
    val resultCodeByUrl = MutableLiveData<String>()


    fun getOauthTpu(webView: WebView) {
        webView.settings.setJavaScriptEnabled(true)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(redirectUrl)
                return true
            }
        }
        webView.loadUrl("https://oauth.tpu.ru/authorize?client_id=39&redirect_uri=${redirectUrl}&response_type=code")
    }

    fun getAnswerOauthTpu(webView: WebView) {
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val url = request.url.toString()
                getCodeAnswerOauthTpu(url)
                App.fullUserTokenUrl = "https://oauth.tpu.ru/access_token?client_id=${App.CLIENT_ID}&client_secret=${App.CLIENT_SECRET}&redirect_uri=${redirectUrl}&code=${App.code}&grant_type=${App.GRANT_TYPE}"
                resultCodeByUrl.value = App.code
                return false
            }
        })
    }

    fun getCodeAnswerOauthTpu(url: String) {

        var code: Boolean = url.toLowerCase().contains("code=".toLowerCase())
        if (code) {
            var str = url.split("code=") as MutableList<String>
            App.code = str[1]
        } else App.code = "error"
    }
}