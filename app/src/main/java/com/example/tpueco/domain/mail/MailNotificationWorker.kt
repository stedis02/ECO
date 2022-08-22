package com.example.tpueco.domain.mail

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tpueco.MainActivity
import com.example.tpueco.R
import com.example.tpueco.data.db.DBManager
import com.example.tpueco.domain.Model.Message
import com.example.tpueco.domain.Model.MessageDate
import com.example.tpueco.presentation.fragment.MailMainFragment
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class MailNotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        try {
            checkNewMessage(applicationContext)
        } catch (e: IOException) {
            return ListenableWorker.Result.retry()
        }

        return Result.success()
    }
}



@RequiresApi(Build.VERSION_CODES.O)
fun checkNewMessage(context: Context) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val email =  sharedPreferences.getString("Email", "error")
    val password =  sharedPreferences.getString("Password", "error")
    while (true) {
        val newMessage = MessageParser.parseMessage(
            Mailer.receive("letter.tpu.ru", 993, email.toString(), password.toString())
                .reversed()[0]
        )
        if (sharedPreferences.getString("lastMessageDate", "") != newMessage.date.time) {
            val editor = sharedPreferences.edit()
            editor.putString("lastMessageDate", newMessage.date.time).commit()
            getNotification(context, newMessage)
        }
    }
}


private const val channelId = "mailMessageId"
private lateinit var notificationManager: NotificationManager


@RequiresApi(Build.VERSION_CODES.O)
fun getNotification(context: Context, message: Message) {
    notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationBuilder = Notification.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("новое письмо от ${message.from}")
        .setContentText(message.header)
        .setPriority(Notification.PRIORITY_HIGH)
        .setCategory(Notification.CATEGORY_SERVICE)
    createChannelIfNeeded(notificationManager)
    notificationManager.notify(Random().nextInt(1000), notificationBuilder.build())

}

fun createChannelIfNeeded(notificationManager: NotificationManager) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel =
            NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}
