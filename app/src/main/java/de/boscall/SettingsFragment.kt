package de.boscall


import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import de.boscall.constants.ServiceConfiguration
import de.boscall.dto.TokenUpdateRequest
import de.boscall.services.BosCallWebAPIService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragment() {
    private val SERVICE = Retrofit.Builder().baseUrl(ServiceConfiguration.API_ADDRESS).addConverterFactory(GsonConverterFactory.create()).build().create(BosCallWebAPIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)

        findPreference("itmUserName").setOnPreferenceChangeListener { preference, newValue ->
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            val userId = sharedPref.getLong("userId", -1)
            val apiKey = sharedPref.getString("apiKey", null)

            if (userId >= 0 && apiKey != null) {
                val userName: String = newValue as String
                val updateTokenRequest = TokenUpdateRequest(userId, FirebaseInstanceId.getInstance().token!!, apiKey, userName)
                // Call service with Fire-And-Forget strategy as name updates are not critical tasks
                SERVICE.updateToken(updateTokenRequest).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {}
                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {}
                })
            }
            true
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        for (pref in sharedPreferences.all.keys) {
            Log.d(this.javaClass.name, "Key ${pref} -> ${(sharedPreferences.all[pref])} -> String? ${(sharedPreferences.all[pref] is String)}")
        }

    }
}// Required empty public constructor
