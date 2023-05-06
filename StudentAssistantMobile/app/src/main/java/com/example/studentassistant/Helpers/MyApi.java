package com.example.studentassistant.Helpers;

import com.example.studentassistant.Model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyApi {
    @POST("api/AuthenticateApi")
    Call<String> authenticateUser(@Body UserModel usersModel);
}
