package com.example.tpueco

///import com.example.tpueco.presentation.Adapter1
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tpueco.data.Network.UsersAPI
import com.example.tpueco.data.db.DBManager
import com.example.tpueco.presentation.StartBrowserAtivity
import com.example.tpueco.presentation.VM.MainViewModel
import com.example.tpueco.presentation.fragment.DocumentCameraFragment
import com.example.tpueco.presentation.fragment.MailLoginFragment
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
        if(userMailAuthorizationCheck()) {
            mainViewModel.runMailWorkers()
        }
    }

    private fun init() {
        dbManager = DBManager(this)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.init()
        appComponent.inject(this)

    }


  private  fun getDataFromUsersAPI() {
        if (tokenAvailabilityCheck()) {
            dbManager.dbOpen()
            mainViewModel.getUserDataByTokenUrl(
                usersAPI,
                "https://api.tpu.ru/v2/auth/user?apiKey=${App.apiKey}&access_token=${dbManager.getTokenData().access_token.toString()}"
            )

            dbManager.dbClose()
        } else {
            getTokenIfBDNull()
        }
    }

  private  fun tokenAvailabilityCheck(): Boolean {
        dbManager.dbOpen()
        val status =
            !(dbManager.getTokenData().access_token == null || dbManager.getTokenData().access_token == "null" || dbManager.getTokenData().access_token == "access_token")
        dbManager.dbClose()
        return status
    }

 private   fun getTokenIfBDNull() {
        if (App.fullUserTokenUrl == "") {
            intent = Intent(this, StartBrowserAtivity::class.java)
            startActivity(intent)
        } else {
            mainViewModel.getAccessToken(usersAPI)
        }
    }


  private  fun addUserTokenToDataBaseIfDataIsReceived() {

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

    private fun userMailAuthorizationCheck(): Boolean{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val email = sharedPreferences.getString("Email", "error")
        return email.toString() != "error"
    }

    fun Click1(view: View) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerMain, DocumentCameraFragment.newInstance())
            .commitNow()

    }

    fun Click2(view: View) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val email = sharedPreferences.getString("Email", "error")
        openMailFragments(email.toString())
    }

    private fun openMailFragments(email : String){
        if(email == "error"){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerMain, MailLoginFragment.newInstance())
                .commitNow()
        } else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerMain, MailMainFragment.newInstance())
                .commitNow()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }


}
