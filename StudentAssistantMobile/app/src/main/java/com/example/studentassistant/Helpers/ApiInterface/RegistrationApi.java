package com.example.studentassistant.Helpers.ApiInterface;

import com.example.studentassistant.Model.LabModel;
import com.example.studentassistant.Model.ResponseModels.AuthenticateResponse;
import com.example.studentassistant.Model.ResponseModels.ResponseModelLab;
import com.example.studentassistant.Model.ResponseModels.ResponseModelSession;
import com.example.studentassistant.Model.ResponseModels.ResponseModelSubject;
import com.example.studentassistant.Model.ResponseModels.ResponseModelUser;
import com.example.studentassistant.Model.ResponseModels.SessionResponse;
import com.example.studentassistant.Model.SessionModel;
import com.example.studentassistant.Model.SubjectModel;
import com.example.studentassistant.Model.UserModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RegistrationApi {
    @POST("RegistrationApi")
    Call<ResponseModelUser> registerUser(@Body UserModel userModel);
    @POST("AddSubjectApi")
    Call<ResponseModelSubject> addSubject(@Body SubjectModel subjectModel);
    @POST("AddLabApi")
    Call<ResponseModelLab> addLab(@Body LabModel labModel);
    @POST("AddSessionApi")
    Call<ResponseModelSession> addSession(@Body SessionModel sessionModel);
    @PUT("UpdateLabApi")
    Call<Void> updateLab(@Body LabModel labModel);
    @PUT("UpdateSubjectApi")
    Call<Void> updateSubject(@Body SubjectModel subjectModel);
    @PUT("UpdateSesssionApi")
    Call<Void> updateSession(@Body SessionModel sessionModel);
    @HTTP(method = "DELETE", path = "DeleteSubjectApi", hasBody = true)
    Call<Void> deleteSubject(@Body SubjectModel subjectModel);
    @HTTP(method = "DELETE", path = "DeleteLabApi", hasBody = true)
    Call<Void> deleteLab(@Body LabModel labModel);
    @POST("GetSessionApi")
    Call<SessionResponse> getSession(@Body UserModel userModel);
    @POST("AuthenticateApi")
    Call<AuthenticateResponse> authenticateUser(@Body UserModel userModel);


}



