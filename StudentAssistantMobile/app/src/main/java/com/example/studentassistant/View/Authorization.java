package com.example.studentassistant.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.studentassistant.DataBase.DbHelper;
import com.example.studentassistant.Helpers.Hash;
import com.example.studentassistant.Helpers.MyApi;
import com.example.studentassistant.Model.UserModel;
import com.example.studentassistant.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
    private MyApi myApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        bind();

        // Создаем объект Retrofit с базовым URL и конвертером для преобразования JSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://example.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Создаем объект API с использованием интерфейса MyApi
        myApi = retrofit.create(MyApi.class);
    }

    private void bind() {
        edtAuthorizationLogin1 = findViewById(R.id.edtAuthorizationLogin1);
        edtAuthorizationLogin = findViewById(R.id.edtAuthorizationLogin);
        edtAuthorizationPassword = findViewById(R.id.edtAuthorizationPassword);
        edtAuthorizationPassword1 = findViewById(R.id.edtAuthorizationPassword1);
        db = new DbHelper(getApplicationContext()).getReadableDatabase();
    }

    private void Auth() {

        //добавить проверки введенных данных
        UserModel user = new UserModel(edtAuthorizationLogin.getText().toString(),  Hash.GetHash(edtAuthorizationPassword.getText().toString()));
        Call<String> call = myApi.authenticateUser(user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String result = response.body();
                    // Обработка ответа
                } else {
                    // Обработка ошибки
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Обработка ошибки
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