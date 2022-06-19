package com.example.tpueco.presentation.VM

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.tpueco.App
import com.example.tpueco.data.Network.UsersAPI
import com.example.tpueco.data.db.DBManager
import com.example.tpueco.domain.Model.UserTokenResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var userTokenResponse = UserTokenResponse()
    var userDataUrl: String = ""
    val dataReceiptCheck = MutableLiveData<Boolean>()

    fun getAccessToken(usersAPI: UsersAPI) {
        compositeDisposable.add(
            usersAPI.getAccessToken(App.fullUserTokenUrl)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    dataReceiptCheck.postValue(true)
                    userTokenResponse = it
                    userDataUrl =
                        "https://api.tpu.ru/v2/auth/user?apiKey=${App.API_KEY}&access_token=${it.access_token}"
                    // мега фича на старт. не забыть потом пересмотреть
                    getUserData(usersAPI, userDataUrl)

                }, {
                    Log.e("tokenResponse", "ErrorResponse")
                })
        )

    }

    fun getUserData(usersAPI: UsersAPI, userDataUrl: String) {

        compositeDisposable.add(
            usersAPI.getUserData(userDataUrl)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.v("aaaa", it.code)
                   // Log.v("aaaa", it.lichnost.familiya)
                   // Log.v("aaaa", it.lichnost.imya)
                   // Log.v("aaaa", it.user_id)
                   // Log.v("aaaa", userDataUrl)


                }, {
                    Log.e("aaaa", "q")

                })
        )

    }

    fun init() {
        dataReceiptCheck.value = false
    }


}