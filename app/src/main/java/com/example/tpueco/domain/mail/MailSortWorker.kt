package com.example.tpueco.domain.mail

import android.content.Context
import android.os.Build
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

class MailSortWorker(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {
    override fun doWork(): Result {
        try {
            Log.v("aaa", "aaa")
            sortMail()
        } catch (e: IOException) {
            return ListenableWorker.Result.retry()
        }
        return Result.success()
    }

    private var findGroup: Boolean = false
    private fun sortMail() {
        var i = 0
        for (message in Mailer.receive("letter.tpu.ru", 993, "sst13@tpu.ru", "McmAkmD7")
            .reversed()) {
            if (i > 50) break
            val newMessage = MessageParser.parseMessage(message)
            findGroup = false
            addMessageToGroupWithSameAddress(newMessage)
            addMessageToNewGroupIfNotFound(newMessage)
            i++
            if (i % 5 == 0) passGroupsToFragment()
        }
    }

    private fun addMessageToGroupWithSameAddress(message: Message) {
        for (group in MainActivity.messageGroups) {
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
            MainActivity.messageGroups.add(newGroup)
        }
    }

    private fun passGroupsToFragment() {
        MailMainFragment.groupLive.postValue(MainActivity.messageGroups)
    }

}