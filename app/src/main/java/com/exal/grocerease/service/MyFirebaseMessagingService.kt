package com.exal.grocerease.service

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // Switch to the main thread to show the Toast
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this, "New token: $token", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notification = message.notification
        val title = notification?.title
        val body = notification?.body
    }
}