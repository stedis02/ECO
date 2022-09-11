package com.example.tpueco.domain.mail

import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tpueco.MainActivity
import com.example.tpueco.domain.Model.Message
import com.example.tpueco.domain.Model.MessageDate
import com.example.tpueco.presentation.fragment.MailMainFragment
import java.io.IOException

class MailReceiveWorker(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {
    override fun doWork(): Result {
        try {
            sortMail(applicationContext)
        } catch (e: IOException) {
            return ListenableWorker.Result.retry()
        }
        return Result.success()
    }

    private var findGroup: Boolean = false

    companion object {
        var messageGroups: MutableList<MutableList<Message>> =
            mutableListOf()
    }

    private fun sortMail(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val email =  sharedPreferences.getString("Email", "error")
        val password =  sharedPreferences.getString("Password", "error")
        var i = 0
        for (message in Mailer.receive("letter.tpu.ru", 993, email.toString(), password.toString())
            .reversed()) {
            if (i > 50) break
            val newMessage = MessageParser.parseMessage(message)
            findGroup = false
            addMessageToGroupWithSameAddress(newMessage)
            addMessageToNewGroupIfNotFound(newMessage)
            i++
            if (i % 5 == 0 || i < 5) passGroupsToFragment()
        }
    }

    private fun addMessageToGroupWithSameAddress(message: Message) {
        for (group in messageGroups) {
            if (group.isNotEmpty()) {
                if (group[0].from == message.from) {
                    group.add(message)
                    findGroup = true
                    break
                }
            }
        }
    }

    private fun addMessageToNewGroupIfNotFound(message: Message) {
        if (!findGroup) {
            val newGroup: MutableList<Message> =
                mutableListOf()
            newGroup.add(message)
            messageGroups.add(newGroup)
        }
    }

    private fun passGroupsToFragment() {
        MailMainFragment.groupLive.postValue(messageGroups)
    }

}