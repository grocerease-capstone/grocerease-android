package com.exal.grocerease.service

import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Toast.makeText(this, "New token: $token", Toast.LENGTH_SHORT).show()
    }
}