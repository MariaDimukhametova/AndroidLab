package com.example.project

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build


class NotificationService (context: Context){
    private val vibPattern = arrayOf(100L, 200L, 0, 400L).toLongArray()
    private val manager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val audio by lazy {
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
    }


    fun showNotification(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                context.getString(R.string.notification_channel_id),
                context.getString(R.string.notification_channel_title),
                IMPORTANCE_DEFAULT
            )
            channel.run {
                vibrationPattern = vibPattern
                setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.alarm), audio)
                description = context.getString(R.string.notification_channel_description)
            }
            manager.createNotificationChannel(channel)
        }
    }
}