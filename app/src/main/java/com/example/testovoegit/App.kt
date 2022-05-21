package com.example.testovoegit

import android.app.Application
import com.example.testovoegit.Network.UsersAPI
import com.example.testovoegit.tools.RetrofitManager

class App : Application(){
    lateinit var usersAPI : UsersAPI
    override fun onCreate() {
        super.onCreate()
        usersAPI = RetrofitManager.GitRetrofit().create(UsersAPI::class.java)
    }
}