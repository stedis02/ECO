package com.example.tpueco

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tpueco.data.db.DBManager
import com.example.tpueco.presentation.VM.MainViewModel
///import com.example.tpueco.presentation.Adapter1
import com.example.tpueco.presentation.StartBrowserAtivity
import com.example.tpueco.presentation.fragment.DocumentCameraFragment

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

    fun Click1(view: View) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerMain, DocumentCameraFragment.newInstance())
            .commitNow()

    }
    fun Click2(view: View) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerMain, DocumentCameraFragment.newInstance())
            .commitNow()

    }

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
