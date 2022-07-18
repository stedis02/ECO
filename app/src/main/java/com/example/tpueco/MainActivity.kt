package com.example.tpueco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tpueco.data.Network.UsersAPI
import com.example.tpueco.data.db.DBManager
import com.example.tpueco.domain.mail.Mailer
import com.example.tpueco.domain.mail.MessageParser
import com.example.tpueco.presentation.VM.MainViewModel
///import com.example.tpueco.presentation.Adapter1
import com.example.tpueco.presentation.StartBrowserAtivity
import com.example.tpueco.presentation.fragment.DocumentCameraFragment
import com.example.tpueco.presentation.fragment.MailMainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var dbManager: DBManager

    @Inject
    lateinit var usersAPI: UsersAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        getDataFromUsersAPI()

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

        runMailService()

    }

    fun init() {
        dbManager = DBManager(this)
        dbManager.dbOpen()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.init()
        appComponent.inject(this)
    }

    fun getDataFromUsersAPI(){
        if(tokenAvailabilityCheck()){
            mainViewModel.getUserDataByTokenUrl(
                usersAPI,
                "https://api.tpu.ru/v2/auth/user?apiKey=${App.API_KEY}&access_token=${dbManager.getTokenData().access_token.toString()}"
            )
        }else{
            getTokenIfBDNull()
        }
    }

    fun tokenAvailabilityCheck() : Boolean {
        return !(dbManager.getTokenData().access_token == null || dbManager.getTokenData().access_token == "null" || dbManager.getTokenData().access_token == "access_token")
    }

    fun getTokenIfBDNull() {
        if (App.fullUserTokenUrl == "") {
            intent = Intent(this, StartBrowserAtivity::class.java)
            startActivity(intent)
        } else {
            mainViewModel.getAccessToken(usersAPI)
        }
    }




    override fun onResume() {
        super.onResume()

    }


    fun runMailService(){
        var messageGroups: MutableList<MutableList<com.example.tpueco.domain.Model.Message>> =
            mutableListOf()

        //Mail receive test
        Thread(Runnable {

            run {
                try {

                    for (message in Mailer.receive("letter.tpu.ru", 993, "sst13@tpu.ru", "McmAkmD7")
                        .reversed()) {
                        var text = "errorText"
                        val newMessage = MessageParser.parseMessage(message)
                        var findGroup = false
                        for (group in messageGroups) {
                            if (group.size != 0) {
                                if (group[0].from == newMessage.from) {
                                    group.add(newMessage)


                                    findGroup = true
                                    break
                                }
                            } else {
                                val newGroup: MutableList<com.example.tpueco.domain.Model.Message> =
                                    mutableListOf()
                                newGroup.add(newMessage)
                                messageGroups.add(newGroup)
                                findGroup = true
                                break
                            }

                        }
                        if (!findGroup){
                            val newGroup: MutableList<com.example.tpueco.domain.Model.Message> =
                                mutableListOf()
                            newGroup.add(newMessage)
                            messageGroups.add(newGroup)
                        }

                        Log.v("vvv", messageGroups.size.toString())



                        MailMainFragment.groupLive.postValue(messageGroups)
                    }

                } catch (e: Exception) {
                    Log.v("ddd", e.stackTraceToString())
                }
            }
        }).start()


    }

    fun Click1(view: View) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerMain, DocumentCameraFragment.newInstance())
            .commitNow()

    }
        fun Click2(view: View) {

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerMain, MailMainFragment.newInstance())
                .commitNow()


      }









}
