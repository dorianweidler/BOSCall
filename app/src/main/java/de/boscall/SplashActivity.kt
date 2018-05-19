package de.boscall

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import de.boscall.constants.ServiceConfiguration
import de.boscall.dto.Registration
import de.boscall.dto.RetrieveUnitsRequest
import de.boscall.services.BosCallWebAPIService
import de.boscall.util.RegistrationStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SplashActivity : AppCompatActivity() {
    private val SERVICE = Retrofit.Builder().baseUrl(ServiceConfiguration.API_ADDRESS).addConverterFactory(GsonConverterFactory.create()).build().create(BosCallWebAPIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // check registered units
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val userId = sharedPref.getLong("userId", -1)
        val apiKey = sharedPref.getString("apiKey", null)

        if (userId >= 0 && apiKey != null) {
            val unitRequest = RetrieveUnitsRequest(userId, apiKey)
            SERVICE.retrieveUnits(unitRequest).enqueue(object : Callback<List<Long>> {
                override fun onFailure(call: Call<List<Long>>?, t: Throwable?) {
                    // no network
                    startApp()
                }

                override fun onResponse(call: Call<List<Long>>?, response: Response<List<Long>>) {
                    val registrations = RegistrationStorage.readRegistrationsFromFile(this@SplashActivity)
                    val updatedRegistrations = mutableListOf<Registration>()
                    Log.d(javaClass.name, "Response: ${response.body()}")
                    for (unitId in response.body()!!) {
                        var found: Registration? = null
                        for (registration in registrations) {
                            if (registration.unitId == unitId) {
                                Log.d(javaClass.name, "UnitIds: ${registration.unitId}   ${unitId}")
                                found = registration
                                break
                            }
                        }
                        if (found != null) {
                            registrations.remove(found)
                            updatedRegistrations.add(found)
                        }
                    }

                    RegistrationStorage.storeRegistrations(this@SplashActivity, updatedRegistrations)

                    startApp()
                }
            })
        } else {
            // first start or invalid settings
            startApp()
        }
    }

    fun startApp() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }
}
