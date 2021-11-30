package com.example.project

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver(){
    private val vibPattern = longArrayOf(0, 2000, 0, 400)

    override fun onReceive(p0: Context, p1: Intent) {
        p1.let {
            if (it.action == "com.test.notification_manager") {
                val intent = Intent(p0, SecondActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val pendingIntent = PendingIntent.getActivity(
                    p0,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                val notification = p0.let { cont ->
                    NotificationCompat.Builder(cont, p0.getString(R.string.notification_channel_id))
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(p0.getString(R.string.notification_content_title))
                        .setContentText(p0.getString(R.string.notification_content_text))
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
                        .setSound(Uri.parse("android.resource://" + p0.packageName + "/" + R.raw.alarm))
                        .setVibrate(vibPattern)
                }
                p0.wakeUpScreen()
                notification.let { empty -> NotificationManagerCompat.from(p0).notify(1, empty.build()) }
            }
        }
    }
}