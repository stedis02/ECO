package com.example.tpueco.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tpueco.App
import com.example.tpueco.MainActivity
import com.example.tpueco.R
import com.example.tpueco.presentation.VM.StartBrowserViewModel

class StartBrowserAtivity : AppCompatActivity() {
    lateinit var startBrowserViewModel: StartBrowserViewModel
    lateinit var webView: WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
        startBrowserViewModel = ViewModelProvider(this).get(StartBrowserViewModel::class.java)
        webView = findViewById(R.id.webviewauth)
        startBrowserViewModel.getOauthTpu(webView)
        startBrowserViewModel.getAnswerOauthTpu(webView)
        startBrowserViewModel.resultCodeByUrl.observe(this, Observer {
            if (it != null && it != "" && it != "error") {
                Log.e("ok", it.toString())
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        })


    }

}