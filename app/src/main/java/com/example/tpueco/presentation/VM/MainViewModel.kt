package com.example.tpueco.presentation.VM

import android.util.Log
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.tpueco.App
import com.example.tpueco.data.Network.UsersAPI
import com.example.tpueco.domain.Model.UserTokenResponse
import com.example.tpueco.domain.mail.MailNotificationWorker
import com.example.tpueco.domain.mail.MailReceiveWorker
import com.google.common.util.concurrent.ListenableFuture
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var userTokenResponse = UserTokenResponse()
    val dataReceiptCheck = MutableLiveData<Boolean>()

    companion object {
        private var userDataBaseUrl =
            "https://api.tpu.ru/v2/auth/user?apiKey=${App.apiKey}&access_token="
        private const val TAGResponse = "Response"
    }

    fun init() {
        dataReceiptCheck.value = false
    }

    fun getAccessToken(usersAPI: UsersAPI) {
        compositeDisposable.add(
            usersAPI.getAccessToken(App.fullUserTokenUrl)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    dataReceiptCheck.postValue(true)
                    userTokenResponse = it
                    Log.e(TAGResponse, "TokenResponse : <user tokens successfully received>")
                    getUserDataByTokenUrl(usersAPI, it.access_token.toString())
                }, {
                    Log.e(
                        TAGResponse,
                        "ErrorTokenResponse : <failed to get user token for some reason>: ${it.message}"
                    )
                })
        )

    }

    fun getUserDataByTokenUrl(usersAPI: UsersAPI, access_token: String) {

        compositeDisposable.add(
            usersAPI.getUserData(userDataBaseUrl + access_token)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.v(
                        TAGResponse,
                        "response received, UserDataResponse came with code: ${it.code}"
                    )
                }, {
                    Log.e(
                        TAGResponse,
                        "ErrorUserDataResponse : <failed to get user token for some reason>: ${it.message}"
                    )

                })
        )

    }


    fun runMailWorkers() {
        runMailNotificationWorker()
        runMailReceiveWorker()
    }



    private fun runMailNotificationWorker() {
        val workManager = WorkManager.getInstance()
        val mailNotificationWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(MailNotificationWorker::class.java)
                .addTag("MailNotificationWorker").build()
        val future: ListenableFuture<List<WorkInfo>> =
            workManager.getWorkInfosByTag("MailNotificationWorker")
        val list: List<WorkInfo> = future.get()
        if (list.isEmpty()) {
            workManager.enqueue(mailNotificationWorkRequest)
        }

    }

    private fun runMailReceiveWorker() {
        val mailReceiveWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(MailReceiveWorker::class.java).build()
        WorkManager.getInstance().enqueue(mailReceiveWorkRequest)
    }



}