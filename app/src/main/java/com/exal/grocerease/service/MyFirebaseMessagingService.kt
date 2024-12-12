package com.exal.grocerease.service

import android.Manifest
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.exal.grocerease.hilt.mainapp.MyApplication
import com.exal.grocerease.view.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var managerCompat: NotificationManagerCompat

    override fun onCreate() {
        super.onCreate()
        managerCompat = NotificationManagerCompat.from(this)
        createNotificationChannel()
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Switch to the main thread to show the Toast
        Handler(Looper.getMainLooper()).post {
//            Toast.makeText(this, "New token: $token", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if(message.notification != null) {
            val title: String? = message.notification?.title
            val body: String? = message.notification?.body

            showNotif(title, body)
        }
    }

    private fun showNotif(title: String?, body: String?) {
        val notif: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title ?: "No Title")
            .setContentText(body ?: "No Content")
            .setSmallIcon(R.drawable.ic_dialog_info) // Replace with your app's notification icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w(TAG, "Notification permission not granted")
            return
        }

        managerCompat.notify(NOTIFICATION_ID, notif)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val descriptionText = "Channel for app notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "1"
        private const val NOTIFICATION_ID = 1
        private const val TAG = "FirebaseMessagingService"
    }
}