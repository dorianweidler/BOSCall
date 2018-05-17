package de.boscall.services;

import de.boscall.dto.Registration;
import de.boscall.dto.RegistrationRequest;
import de.boscall.dto.UnregistrationRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.POST;

public interface BosCallWebAPIService {
    @POST("registration")
    Call<Registration> registerUnit(@Body RegistrationRequest request);

    @HTTP(method = "DELETE", hasBody = true, path = "registration")
    Call<Registration> unregisterUnit(@Body UnregistrationRequest request);
}
