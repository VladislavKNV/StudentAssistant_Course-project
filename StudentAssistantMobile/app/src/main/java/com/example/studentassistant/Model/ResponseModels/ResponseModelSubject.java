package com.example.studentassistant.Model.ResponseModels;

import com.example.studentassistant.Model.SubjectModel;
import com.example.studentassistant.Model.UserModel;
import com.example.studentassistant.View.Subjects;
import com.google.gson.annotations.SerializedName;

public class ResponseModelSubject {
    @SerializedName("SubjectModel")
    private SubjectModel subjectModel;

    public SubjectModel getSubjectModel() {
        return subjectModel;
    }
}
