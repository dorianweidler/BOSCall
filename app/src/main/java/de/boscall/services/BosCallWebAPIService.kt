package de.boscall.services

import de.boscall.dto.Registration
import de.boscall.dto.RegistrationRequest
import de.boscall.dto.UnregistrationRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.POST

interface BosCallWebAPIService {
    @POST("registration")
    fun registerUnit(@Body request: RegistrationRequest): Call<Registration>

    @HTTP(method = "DELETE", hasBody = true, path = "registration")
    fun unregisterUnit(@Body request: UnregistrationRequest): Call<ResponseBody>
}