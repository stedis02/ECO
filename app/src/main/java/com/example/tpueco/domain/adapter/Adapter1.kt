package com.example.tpueco.domain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpueco.R
import com.squareup.picasso.Picasso
/**
class Adapter1( var context: Context ): RecyclerView.Adapter<Adapter1.MainViewHolder>() {

     lateinit var userList: MutableList<UserResponse>

init {
    userList = mutableListOf()
}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
       var  view = LayoutInflater.from(context).inflate(R.layout.user, parent, false )
        return MainViewHolder(view, context, userList)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.setData(userList.get(position))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateAdapter(userList1: List<UserResponse>){
        userList.clear()
        userList.addAll(userList1)
        notifyDataSetChanged();

    }

    class MainViewHolder(var  itemView: View, var context: Context,  var userList: List<UserResponse> ) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        lateinit var login: TextView
        lateinit var iduser : TextView
        lateinit var image : ImageView
        init {
            login = itemView.findViewById(R.id.login)
            iduser = itemView.findViewById(R.id.iduser)
            image = itemView.findViewById(R.id.userimage)

        }
fun setData(userResponse: UserResponse){
    login.text = userResponse.login
    iduser.text = userResponse.id.toString()
   Picasso.get().load(userResponse.avatar_url).into(image)
}
        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }

    }



}
 **/