package com.example.tpueco

import android.app.Application
import android.content.Context
import com.example.tpueco.data.Network.UsersAPI
import com.example.tpueco.data.tools.RetrofitManager

class App : Application() {
    lateinit var usersAPI: UsersAPI

    companion object {
        var oauthTpuCheck: Boolean = false
        var authorizeСode: String = ""
        val API_KEY: String = "8f0afb6b47e2193836a95aaf9dea703c"
        var ACCESS_TOKEN: String = ""
        val CLIENT_ID: String = "39"
        val CLIENT_SECRET: String = "Z-t-6hwS"
        val GRANT_TYPE: String = "authorization_code"
        var fullUserTokenUrl: String = ""
    }

    override fun onCreate() {
        super.onCreate()
        usersAPI = RetrofitManager.GitRetrofit().create(UsersAPI::class.java)


    }
}