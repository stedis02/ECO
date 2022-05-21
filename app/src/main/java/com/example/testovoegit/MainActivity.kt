package com.example.testovoegit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testovoegit.Model.UserResponse
import com.example.testovoegit.VM.MainVM

class MainActivity : AppCompatActivity() {

    lateinit var mainVM: MainVM
    lateinit var  recyclerView: RecyclerView
    lateinit var  adapter1: Adapter1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter1 = Adapter1(this)
        mainVM = ViewModelProvider(this).get(MainVM::class.java)
        mainVM.FetchUser((application as App).usersAPI)
        recyclerView = findViewById(R.id.userList)
        recyclerView.layoutManager = LinearLayoutManager(this)
       recyclerView.adapter = adapter1
    }

    fun Click1(view: View){
        mainVM.getUS()?.let { adapter1.updateAdapter(it) }
    }
}