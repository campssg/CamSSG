package com.example.graduationproject.Owner

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.R

class AddressActivity : AppCompatActivity() {
    companion object {
        const val ADDRESS_REQUEST_CODE = 2928
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1_address)

        val webView = findViewById<WebView>(R.id.web_view)

        webView.settings.javaScriptEnabled = true

        webView.addJavascriptInterface(KaKaoJavaScriptInterface(), "Android")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                webView.loadUrl("javascript:execKakaoPostcode();")
            }
        }

        // Kakao에서 https를 허용하지 않아서 https -> http로 바꿔야 동작함 (중요!!)
        webView.loadUrl("http://13.124.13.202:8080/index.html")
    }

    inner class KaKaoJavaScriptInterface {

        @JavascriptInterface
        fun processDATA(address: String?) {
            Intent().apply {
                putExtra("address", address)
                setResult(RESULT_OK, this)
            }
            finish()
        }
    }
}