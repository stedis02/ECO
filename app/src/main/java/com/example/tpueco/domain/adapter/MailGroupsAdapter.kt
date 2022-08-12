package com.example.tpueco.domain.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpueco.MainActivity
import com.example.tpueco.R
import com.example.tpueco.domain.Model.Message
import com.example.tpueco.presentation.fragment.MailFragment

class MailGroupsAdapter(var context: Context) :
    RecyclerView.Adapter<MailGroupsAdapter.MainViewHolder>() {
    var messageGroups: MutableList<MutableList<Message>> =
        arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.mail_main_item, parent, false)
        return MailGroupsAdapter.MainViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val date = messageGroups[position][0].date.day + " " + messageGroups[position][0]
            .date.month
        holder.setData(
            position,
            messageGroups[position][0].from,
            messageGroups[position][0].header,
            date,
            messageGroups[position][0].from.substring(0, 2)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newMessageGroups: MutableList<MutableList<com.example.tpueco.domain.Model.Message>>) {
        messageGroups.clear()
        messageGroups.addAll(newMessageGroups)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messageGroups.size
    }

    class MainViewHolder(itemView: View, var context: Context) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var messageMessageHeader: TextView = itemView.findViewById(R.id.mailMainHeader)
        var messageGroupLastText: TextView = itemView.findViewById(R.id.mailMainText)
        var messageGroupLastDate: TextView = itemView.findViewById(R.id.mailMainDate)
        var messageGroupIconText: TextView = itemView.findViewById(R.id.mainIconText)
        var posision: Int = 0

        init {
            itemView.setOnClickListener(this)
        }

        fun setData(
            _position: Int,
            _messageMessageHeader: String,
            _messageGroupLastText: String,
            _messageGroupLastDate: String,
            _messageGroupIconText: String,
        ) {
            posision = _position
            messageMessageHeader.text = _messageMessageHeader
            messageGroupLastText.text = _messageGroupLastText
            messageGroupLastDate.text = _messageGroupLastDate
            messageGroupIconText.text = _messageGroupIconText

        }

        override fun onClick(p0: View?) {

            val fragment = MailFragment()
            val bundle = Bundle()
            bundle.putInt("position", posision)
            fragment.arguments = bundle

            val mainActivity: MainActivity = context as MainActivity
            mainActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerMain, fragment)
                .commitNow()
        }

    }

}