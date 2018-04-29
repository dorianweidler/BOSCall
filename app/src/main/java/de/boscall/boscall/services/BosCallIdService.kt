package de.boscall.boscall.services

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class BosCallIdService : FirebaseInstanceIdService() {

    val TAG = this.javaClass.name

    override fun onTokenRefresh() {
        Log.d(TAG, "TOKEN REFRESH")
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken)
    }

    fun sendRegistrationToServer(token: String) {
        Log.d(TAG, "Registration sent ${token}")
    }

}