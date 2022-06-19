package com.example.tpueco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpueco.data.db.DBManager
import com.example.tpueco.presentation.VM.MainViewModel
///import com.example.tpueco.presentation.Adapter1
import com.example.tpueco.presentation.StartBrowserAtivity

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var dbManager: DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        tokenAvailabilityCheck()
        mainViewModel.dataReceiptCheck.observe(this, Observer {
            if (it == true) {
                dbManager.dbInsertTokenData(
                    mainViewModel.userTokenResponse.access_token.toString(),
                    mainViewModel.userTokenResponse.refresh_token.toString(),
                    mainViewModel.userTokenResponse.expires_in.toString()
                )
            } else {
            }
        })
    }


    override fun onResume() {
        super.onResume()
    }

    fun Click1(view: View) {}


    fun getTokenIfBDNull() {
        if (App.fullUserTokenUrl == "") {
            intent = Intent(this, StartBrowserAtivity::class.java)
            startActivity(intent)
        } else {
            mainViewModel.getAccessToken((application as App).usersAPI)
        }
    }

    fun init() {
        dbManager = DBManager(this)
        dbManager.dbOpen()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.init()
    }

    fun tokenAvailabilityCheck() {
        if (dbManager.getTokenData().access_token == null || dbManager.getTokenData().access_token == "null" || dbManager.getTokenData().access_token == "access_token") {
            getTokenIfBDNull()
        } else {
            mainViewModel.getUserData(
                (application as App).usersAPI,
                "https://api.tpu.ru/v2/auth/user?apiKey=${App.API_KEY}&access_token=${dbManager.getTokenData().access_token.toString()}"
            )
        }
    }
}
