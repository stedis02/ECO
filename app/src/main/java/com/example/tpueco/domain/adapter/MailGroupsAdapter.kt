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

class MailGroupsAdapter(var context: Context) :
    RecyclerView.Adapter<MailGroupsAdapter.MainViewHolder>() {
    var messageGroup: MutableList<MutableList<com.example.tpueco.domain.Model.Message>> =
        arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.mail_main_item, parent, false)
        return MailGroupsAdapter.MainViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        var date = messageGroup.get(position).get(0).date.day + " " + messageGroup.get(position)
            .get(0).date.month
        holder.setData(position,
            messageGroup.get(position).get(0).from,
            messageGroup.get(position).get(0).Text.toString(),
            date,
            messageGroup.get(position).get(0).from.substring(0, 2)
        )
    }

    fun updateAdapter(newMessageGroups: MutableList<MutableList<com.example.tpueco.domain.Model.Message>>) {
        messageGroup.clear()
        messageGroup.addAll(newMessageGroups)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messageGroup.size
    }

    class MainViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var messageMessageHeader: TextView = itemView.findViewById(R.id.mailMainHeader)
        var messageGroupLastText: TextView = itemView.findViewById(R.id.mailMainText)
        var messageGroupLastDate: TextView = itemView.findViewById(R.id.mailMainDate)
        var messageGroupIconText: TextView = itemView.findViewById(R.id.mainIconText)
        var context: Context = context
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
          //  Toast.makeText(context, "Введите имя файла!", Toast.LENGTH_SHORT).show()




            val fragment = MailFragment()
            val bundle = Bundle()
            bundle.putInt("position", posision)
            fragment.arguments = bundle

                  var mainActivity: MainActivity = context as MainActivity
            mainActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerMain, fragment)
               .commitNow()
        }

    }

}