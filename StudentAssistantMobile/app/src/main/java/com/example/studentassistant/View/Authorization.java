package com.example.studentassistant.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.studentassistant.DataBase.DbHelper;
import com.example.studentassistant.Helpers.ApiInterface.RegistrationApi;
import com.example.studentassistant.Helpers.Hash;
import com.example.studentassistant.Model.LabModel;
import com.example.studentassistant.Model.ResponseModels.AuthenticateResponse;
import com.example.studentassistant.Model.SessionModel;
import com.example.studentassistant.Model.SubjectModel;
import com.example.studentassistant.Model.UserModel;
import com.example.studentassistant.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Authorization extends AppCompatActivity {

    private TextInputEditText edtAuthorizationLogin;
    private TextInputLayout edtAuthorizationLogin1;
    private TextInputEditText edtAuthorizationPassword;
    private TextInputLayout edtAuthorizationPassword1;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        bind();


    }

    private void bind() {
        edtAuthorizationLogin1 = findViewById(R.id.edtAuthorizationLogin1);
        edtAuthorizationLogin = findViewById(R.id.edtAuthorizationLogin);
        edtAuthorizationPassword = findViewById(R.id.edtAuthorizationPassword);
        edtAuthorizationPassword1 = findViewById(R.id.edtAuthorizationPassword1);
        db = new DbHelper(getApplicationContext()).getReadableDatabase();
    }

    public void Auth(View view) {
        try {
            aaa();
        } catch (Exception e) {
            Log.e("DeleteLabApi", "Failed to delete lab", e);
        }


    }

    public void aaa() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://93.125.10.36/Test/api/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegistrationApi api = retrofit.create(RegistrationApi.class);

        UserModel userModel = new UserModel(edtAuthorizationLogin.getText().toString(), Hash.GetHash(edtAuthorizationPassword.getText().toString()));
        Call<AuthenticateResponse> call = api.authenticateUser(userModel);
        call.enqueue(new Callback<AuthenticateResponse>() {
            @Override
            public void onResponse(Call<AuthenticateResponse> call, Response<AuthenticateResponse> response) {
                if (response.isSuccessful()) {
                    AuthenticateResponse authenticateResponse = response.body();
                    UserModel userModel = authenticateResponse.userModel;
                    List<SubjectModel> subjectsList = authenticateResponse.subjectsList;
                    List<LabModel> labsList = authenticateResponse.labsList;
                    List<SessionModel> sessionList = authenticateResponse.sessionList;

                    // Вы можете здесь использовать полученные данные для отображения на экране
                } else {
                    // Обработка ошибки
                }
            }

            @Override
            public void onFailure(Call<AuthenticateResponse> call, Throwable t) {
                // Обработка ошибки
                Log.e("DeleteLabApi", "Failed to delete lab", t);
            }
        });
    }

    public void GoToRegister(View view) {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        // Здесь ничего не делаем
    }
}