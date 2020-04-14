package com.jrlabls.auto_drip.services

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jrlabls.auto_drip.ui.MainActivity

class FirebaseMessaging: FirebaseMessagingService() {

    companion object {
        const val TAG = "fcmMessage"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "Message data payload: " + remoteMessage.data)
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
        }


        val myNotificationManager = MyNotificationManager(applicationContext)
        myNotificationManager.showNotification(remoteMessage.from!!, remoteMessage.notification!!.body!!,
            Intent(applicationContext, MainActivity::class.java)
        )
    }


}