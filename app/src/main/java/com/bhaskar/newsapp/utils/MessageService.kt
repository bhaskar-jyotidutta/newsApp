package com.bhaskar.newsapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bhaskar.newsapp.R
import com.bhaskar.newsapp.presentation.activities.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessageService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        handleDataMessage(message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


    private fun handleDataMessage(message: RemoteMessage) {
        val value1 = message.data["key1"] //Handle data message
        val value2 = message.data["key1"] //Handle data message
        if(message.notification != null){
            val title = message.notification!!.title
            val body = message.notification!!.body
            showNotification(title!!, body!!)
        }
    }

    /**
     * TO show notification when the app is in foreground
     */
    private fun showNotification(title: String, message: String) {
        val channelId = "newsChannel"
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory. decodeResource (resources ,
                R.mipmap.ic_launcher_foreground
            ))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, "channel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, notificationBuilder.build())

    }


}