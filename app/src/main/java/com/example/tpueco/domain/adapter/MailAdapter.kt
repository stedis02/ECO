package com.example.tpueco.domain.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpueco.MainActivity
import com.example.tpueco.R
import com.example.tpueco.presentation.fragment.MailFragment

class MailAdapter(val context: Context) :
    RecyclerView.Adapter<MailAdapter.MainViewHolder>() {
    var messages: MutableList<com.example.tpueco.domain.Model.Message> =
        arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.mail_item, parent, false)
        return MailAdapter.MainViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        var date = messages.get(position).date.day + " " + messages.get(position)
            .date.month + " " + messages.get(position).date.time
        holder.setData(
            messages.get(position).header, messages.get(position).Text.toString(), date
        )
    }

    fun updateAdapter(newMessages: MutableList<com.example.tpueco.domain.Model.Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MainViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        var messageMessageHeader: TextView = itemView.findViewById(R.id.mailMainHeader)
        var messageGroupLastText: TextView = itemView.findViewById(R.id.mailMainText)
        var messageGroupLastDate: TextView = itemView.findViewById(R.id.mailMainDate)
        var context: Context = context


        init {

        }

        fun setData(
            _messageMessageHeader: String,
            _messageGroupLastText: String,
            _messageGroupLastDate: String,
        ) {

            messageMessageHeader.text = _messageMessageHeader
            messageGroupLastText.text = _messageGroupLastText
            messageGroupLastDate.text = _messageGroupLastDate

        }


    }


}