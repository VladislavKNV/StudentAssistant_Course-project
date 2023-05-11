package com.example.studentassistant.Model.ResponseModels;

import com.example.studentassistant.Model.LabModel;
import com.example.studentassistant.Model.SessionModel;
import com.example.studentassistant.Model.SubjectModel;
import com.example.studentassistant.Model.UserModel;

import java.util.List;

public class AuthenticateResponse {
    public UserModel userModel;
    public List<SubjectModel> subjectsList;
    public List<LabModel> labsList;
    public List<SessionModel> sessionList;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public List<SubjectModel> getSubjectsList() {
        return subjectsList;
    }

    public void setSubjectsList(List<SubjectModel> subjectsList) {
        this.subjectsList = subjectsList;
    }

    public List<LabModel> getLabsList() {
        return labsList;
    }

    public void setLabsList(List<LabModel> labsList) {
        this.labsList = labsList;
    }

    public List<SessionModel> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<SessionModel> sessionList) {
        this.sessionList = sessionList;
    }
}
