package com.example.tpueco.presentation.VM

import android.util.Log
import androidx.lifecycle.*
import com.example.tpueco.App
import com.example.tpueco.data.Network.UsersAPI
import com.example.tpueco.domain.Model.UserTokenResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var userTokenResponse = UserTokenResponse()
    val dataReceiptCheck = MutableLiveData<Boolean>()

    fun getAccessToken(usersAPI: UsersAPI) {
        compositeDisposable.add(
            usersAPI.getAccessToken(App.fullUserTokenUrl)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    dataReceiptCheck.postValue(true)
                    userTokenResponse = it
                    Log.e(TAGResponse, "TokenResponse : <user tokens successfully received>")
                    // мега фича на старт. не забыть потом пересмотреть
                    getUserData(usersAPI, it.access_token.toString())
                }, {
                    Log.e(TAGResponse, "ErrorTokenResponse : <failed to get user token for some reason>: ${it.message}")
                })
        )

    }

    fun getUserData(usersAPI: UsersAPI, access_token: String) {

        compositeDisposable.add(
            usersAPI.getUserData( userDataBaseUrl + access_token)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.v(TAGResponse, "response received, UserDataResponse came with code: ${it.code}")
                }, {
                    Log.e(TAGResponse, "ErrorUserDataResponse : <failed to get user token for some reason>: ${it.message}")

                })
        )

    }

    fun init() {
        dataReceiptCheck.value = false
    }

companion object{
    private var userDataBaseUrl =
    "https://api.tpu.ru/v2/auth/user?apiKey=${App.API_KEY}&access_token="
    private const val TAGResponse = "Response"
}
}