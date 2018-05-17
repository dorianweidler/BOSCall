package de.boscall.services

import de.boscall.dto.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HTTP

const val REGISTRATION_ENDPOINT = "registration"
const val TOKEN_UPDATE_ENDPOINT = "updateToken"
const val UNITS_ENDPOINT = "units"

interface BosCallWebAPIService {

    @HTTP(method = "POST", hasBody = true, path = REGISTRATION_ENDPOINT)
    fun registerUnit(@Body request: RegistrationRequest): Call<Registration>

    @HTTP(method = "DELETE", hasBody = true, path = REGISTRATION_ENDPOINT)
    fun unregisterUnit(@Body request: UnregistrationRequest): Call<ResponseBody>

    @HTTP(method = "POST", hasBody = true, path = TOKEN_UPDATE_ENDPOINT)
    fun updateToken(@Body request: TokenUpdateRequest): Call<ResponseBody>

    @HTTP(method = "POST", hasBody = true, path = UNITS_ENDPOINT)
    fun retrieveUnits(@Body request: RetrieveUnitsRequest): Call<List<Long>>
}