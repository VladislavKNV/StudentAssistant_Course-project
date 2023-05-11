package com.example.studentassistant.Model.ResponseModels;

import com.example.studentassistant.Model.SessionModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SessionResponse {
    @SerializedName("SessionModel")
    private List<SessionModel> sessionList;

    public List<SessionModel> getSessionList() {
        return sessionList;
    }
}
