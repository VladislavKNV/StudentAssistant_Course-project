package com.example.studentassistant.Model.ResponseModels;

import com.example.studentassistant.Model.SessionModel;
import com.example.studentassistant.Model.UserModel;
import com.google.gson.annotations.SerializedName;

public class ResponseModelSession {
    @SerializedName("SessionModel")
    private SessionModel sessionModel;

    public SessionModel getSessionModel() {
        return sessionModel;
    }
}
