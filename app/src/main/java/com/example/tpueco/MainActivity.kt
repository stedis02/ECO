package com.example.tpueco

///import com.example.tpueco.presentation.Adapter1
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.tpueco.data.Network.UsersAPI
import com.example.tpueco.data.db.DBManager
import com.example.tpueco.domain.mail.MailNotificationWorker
import com.example.tpueco.domain.mail.MailSortWorker
import com.example.tpueco.presentation.StartBrowserAtivity
import com.example.tpueco.presentation.VM.MainViewModel
import com.example.tpueco.presentation.fragment.DocumentCameraFragment
import com.example.tpueco.presentation.fragment.MailMainFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    lateinit var dbManager: DBManager

    @Inject
    lateinit var usersAPI: UsersAPI


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        getDataFromUsersAPI()
        addUserTokenToDataBaseIfDataIsReceived()
        runMailWorkers()
    }

    private fun init() {
        dbManager = DBManager(this)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.init()
        appComponent.inject(this)

    }


    fun getDataFromUsersAPI() {
        if (tokenAvailabilityCheck()) {
            dbManager.dbOpen()
            mainViewModel.getUserDataByTokenUrl(
                usersAPI,
                "https://api.tpu.ru/v2/auth/user?apiKey=${App.API_KEY}&access_token=${dbManager.getTokenData().access_token.toString()}"
            )

            dbManager.dbClose()
        } else {
            getTokenIfBDNull()
        }
    }

    fun tokenAvailabilityCheck(): Boolean {
        dbManager.dbOpen()
        val status =
            !(dbManager.getTokenData().access_token == null || dbManager.getTokenData().access_token == "null" || dbManager.getTokenData().access_token == "access_token")
        dbManager.dbClose()
        return status
    }

    fun getTokenIfBDNull() {
        if (App.fullUserTokenUrl == "") {
            intent = Intent(this, StartBrowserAtivity::class.java)
            startActivity(intent)
        } else {
            mainViewModel.getAccessToken(usersAPI)
        }
    }


    fun addUserTokenToDataBaseIfDataIsReceived() {

        mainViewModel.dataReceiptCheck.observe(this, Observer {
            if (it == true) {
                dbManager.dbOpen()
                dbManager.dbInsertTokenData(
                    mainViewModel.userTokenResponse.access_token.toString(),
                    mainViewModel.userTokenResponse.refresh_token.toString(),
                    mainViewModel.userTokenResponse.expires_in.toString()
                )
                dbManager.dbClose()
            }
        })
    }

    fun runMailWorkers() {
        runMailNotificationWorker()
        runMailSortWorker()
    }

    fun runMailNotificationWorker() {
        val MailNotificationWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(MailNotificationWorker::class.java).build()
        WorkManager.getInstance().enqueue(MailNotificationWorkRequest)
    }

    fun runMailSortWorker() {
        val MailSortWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(MailSortWorker::class.java).build()
        WorkManager.getInstance().enqueue(MailSortWorkRequest)
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


    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        var messageGroups: MutableList<MutableList<com.example.tpueco.domain.Model.Message>> =
            mutableListOf()
    }


}
