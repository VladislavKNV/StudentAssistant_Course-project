package com.example.studentassistant.Model.ResponseModels;

import com.example.studentassistant.Model.LabModel;
import com.example.studentassistant.Model.SubjectModel;
import com.google.gson.annotations.SerializedName;

public class ResponseModelLab {
    @SerializedName("LabModel")
    private LabModel labModel;

    public LabModel getLabModel() {
        return labModel;
    }
}
