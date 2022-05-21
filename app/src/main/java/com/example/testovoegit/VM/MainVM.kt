package com.example.testovoegit.VM

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.testovoegit.Model.UserResponse
import com.example.testovoegit.Network.UsersAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainVM: ViewModel() {
    private val compositeDisposable = CompositeDisposable()
     var us: List<UserResponse>? = null

    fun getUS(): List<UserResponse>? {return  us}

    fun FetchUser(usersAPI: UsersAPI){
        compositeDisposable.add(usersAPI.GetUserResponse()
            .subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({
                 Log.e("aaaa", "ok")
                us = it

                Log.e("aaaa", us?.get(0)?.login.toString())
                Log.e("aaaa",  us?.get(1)?.login.toString())

            },{
                Log.e("aaaa", "dlba`b")
            })
        )

    }

}