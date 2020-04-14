package com.jrlabls.auto_drip.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jrlabls.auto_drip.R

class MyNotificationManager(private val mContext: Context) {

    companion object {
        const val NOTIFICATION_ID = 101
    }

    fun showNotification(title: String, notification: String, intent: Intent) {

        val pendingIntent = PendingIntent.getActivity(mContext, NOTIFICATION_ID,
            intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(mContext, NOTIFICATION_ID.toString())
        val mNotification = notificationBuilder.setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_group_15)
            .setContentTitle(title)
            .setContentText(notification)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        mNotification.flags = Notification.FLAG_AUTO_CANCEL

        NotificationManagerCompat.from(mContext).notify(NOTIFICATION_ID, mNotification)
    }
}