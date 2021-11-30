package com.example.project

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.PowerManager

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


    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                context.getString(R.string.notification_channel_id),
                context.getString(R.string.notification_channel_title),
                IMPORTANCE_DEFAULT
            )
            channel.apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                vibrationPattern = vibPattern
                setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.alarm), audio)
                description = context.getString(R.string.notification_channel_description)
            }
            manager.createNotificationChannel(channel)
        }
    }
}

fun Context.wakeUpScreen() {
    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager

    if (!powerManager.isInteractive) {
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_DIM_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "com.example:notificationLock"
        )
        wakeLock.acquire(3000)
    }
}