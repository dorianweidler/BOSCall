package de.boscall.services

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import de.boscall.R
import de.boscall.constants.ServiceConfiguration
import de.boscall.dto.TokenUpdateRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BosCallIdService : FirebaseInstanceIdService() {

    val TAG = this.javaClass.name
    private val SERVICE = Retrofit.Builder().baseUrl(ServiceConfiguration.API_ADDRESS).addConverterFactory(GsonConverterFactory.create()).build().create(BosCallWebAPIService::class.java)

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
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val userId = sharedPref.getLong("userId", -1)
        val apiKey = sharedPref.getString("apiKey", null)

        if (userId >= 0 && apiKey != null) {
            val userName = sharedPref.getString("itmUserName", getString(R.string.itmDefault_userName))
            val updateTokenRequest = TokenUpdateRequest(userId, token, apiKey, userName)
            SERVICE.updateToken(updateTokenRequest).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    Log.d(javaClass.name, "Updating token failed")
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response != null && response.code() == 200) {
                        Log.d(javaClass.name, "Updated token successfully")
                    } else {
                        Log.d(javaClass.name, "Updating token failed")
                    }
                }
            })
        } else {
            // nothing to update
        }
    }

}