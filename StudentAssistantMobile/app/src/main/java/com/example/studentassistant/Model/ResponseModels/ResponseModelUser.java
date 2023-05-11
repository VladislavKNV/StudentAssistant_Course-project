package com.example.studentassistant.Model.ResponseModels;

import com.example.studentassistant.Model.UserModel;
import com.google.gson.annotations.SerializedName;

public class ResponseModelUser {
    @SerializedName("UserModel")
    private UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }
}