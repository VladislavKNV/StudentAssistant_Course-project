package com.example.studentassistant.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.studentassistant.DataBase.DbHelper;
import com.example.studentassistant.DataBase.DbUser;
import com.example.studentassistant.Model.UserModel;
import com.example.studentassistant.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {

    private TextInputLayout edtRegistrationLogin1;
    private TextInputEditText edtRegistrationLogin;
    private TextInputLayout edtRegistrationEmail1;
    private TextInputEditText edtRegistrationEmail;
    private TextInputLayout edtRegistrationPassword11;
    private TextInputEditText edtRegistrationPassword1;
    private TextInputLayout edtRegistrationPassword12;
    private TextInputEditText edtRegistrationPassword2;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        bind();

        //слушатели на поля для ввода
        edtRegistrationLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 3) {
                    edtRegistrationLogin1.setError("Не менее 3 символов");
                } else {
                    edtRegistrationLogin1.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void bind() {
        edtRegistrationLogin1 = findViewById(R.id.edtRegistrationLogin1);
        edtRegistrationLogin = findViewById(R.id.edtRegistrationLogin);
        edtRegistrationEmail1 = findViewById(R.id.edtRegistrationEmail1);
        edtRegistrationEmail = findViewById(R.id.edtRegistrationEmail);
        edtRegistrationPassword11 = findViewById(R.id.edtRegistrationPassword11);
        edtRegistrationPassword1 = findViewById(R.id.edtRegistrationPassword1);
        edtRegistrationPassword12 = findViewById(R.id.edtRegistrationPassword12);
        edtRegistrationPassword2 = findViewById(R.id.edtRegistrationPassword2);
        db = new DbHelper(getApplicationContext()).getReadableDatabase();
    }

    public void Reg(View view) {

        if (isEmailValid(edtRegistrationEmail.getText().toString())) {
            // email валиден, продолжаем выполнение кода
        } else {
            // email не валиден, выводим сообщение об ошибке
            edtRegistrationEmail1.setError("Некорректный email");
        }
        if (isEmailValid(edtRegistrationEmail.getText().toString()) && isLoginValid(edtRegistrationEmail.getText().toString()) &&
                isPasswordValid(edtRegistrationPassword1.getText().toString(), edtRegistrationPassword2.getText().toString())) {

            try {
                UserModel userModel = new UserModel(1, edtRegistrationLogin.getText().toString(), edtRegistrationEmail.getText().toString(), edtRegistrationPassword1.getText().toString());
                if(DbUser.add(db, userModel) != -1) {
                    Toast.makeText(this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else Toast.makeText(this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isPasswordValid (String password1, String password2) {
        if (password1.trim().length() < 6 || password1.trim().length() > 50) {
            edtRegistrationPassword11.setError("Не менее 6 символов и не более 50");
            edtRegistrationPassword12.setError("Не менее 6 символов и не более 50");
            return false;
        }
        if (password1.equals(password2)) {
            return true;
        } else {
            edtRegistrationPassword11.setError("Пароли не совпадают");
            edtRegistrationPassword12.setError("Пароли не совпадают");
        }
        return false;
    }

    public boolean isLoginValid (String login) {
        if (login.trim().length() < 3 || login.trim().length() > 50) {
            return false;
        }
        return true;
    }

    public boolean isEmailValid(String email) {
        // регулярное выражение для проверки email
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Authorization.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}