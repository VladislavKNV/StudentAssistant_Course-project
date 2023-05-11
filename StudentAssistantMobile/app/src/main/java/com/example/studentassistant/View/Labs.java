package com.example.studentassistant.View;

import static com.example.studentassistant.DataBase.DbSession.getSessionBySubjectId;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentassistant.DataBase.DbHelper;
import com.example.studentassistant.DataBase.DbLabs;
import com.example.studentassistant.DataBase.DbSession;
import com.example.studentassistant.DataBase.DbSubjects;
import com.example.studentassistant.DataBase.DbUser;
import com.example.studentassistant.Helpers.ApiInterface.RegistrationApi;
import com.example.studentassistant.Model.LabModel;
import com.example.studentassistant.Model.ResponseModels.ResponseModelLab;
import com.example.studentassistant.Model.SessionModel;
import com.example.studentassistant.Model.SubjectModel;
import com.example.studentassistant.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Labs extends AppCompatActivity {

    private SQLiteDatabase db;
    private SQLiteDatabase db2;
    private ListView viewLabs;
    private ArrayList<LabModel> labs;
    private LabsListAdapter labsListAdapter;
    private int subjectId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labs);

        Intent intent = getIntent();
        subjectId = (Integer) intent.getSerializableExtra("subjectIdLab");
        String titleTxt = (String) intent.getSerializableExtra("subjectNameLab");
        userId = (Integer) intent.getSerializableExtra("userIdIntent");


        setTitle(titleTxt);
        bind();
        getLabs();
        labsListAdapter = new LabsListAdapter(this, labs);
        viewLabs.setAdapter(labsListAdapter);
    }

    private void bind() {
        db = new DbHelper(getApplicationContext()).getReadableDatabase();
        db2 = new DbHelper(getApplicationContext()).getWritableDatabase();
        viewLabs = findViewById(R.id.listViewLabs);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_labs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.btAddLab:
                //
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://93.125.10.36/Test/api/rest/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RegistrationApi registrationApi = retrofit.create(RegistrationApi.class);

                LabModel labModel = new LabModel(subjectId, 0);

                Call<ResponseModelLab> call = registrationApi.addLab(labModel);
                call.enqueue(new Callback<ResponseModelLab>() {
                    @Override
                    public void onResponse(Call<ResponseModelLab> call, Response<ResponseModelLab> response) {
                        if (response.isSuccessful()) {
                            ResponseModelLab responseModel = response.body();
                            LabModel labModel = responseModel.getLabModel();

                            int id = labModel.getId(); // Получаем идентификатор из объекта UserModel
                            labModel.setId(id); // Устанавливаем идентификатор в объекте UserModel
                            if(DbLabs.add(db, labModel) != -1) {
                                getLabs();
                                labsListAdapter = new LabsListAdapter(getApplicationContext(), labs);
                                viewLabs.setAdapter(labsListAdapter);
                            } else Toast.makeText(getApplicationContext(), "Ошибка добавления", Toast.LENGTH_SHORT).show();
                        } else {
                            // Обработка ошибки
                            Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseModelLab> call, Throwable t) {
                        // Обработка ошибки
                        Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                    }
                });
                //
                return true;
            case R.id.btDelLab:
                int count = labs.size() - 1;
                deleteLabApi(labs.get(count).getId());
                getLabs();
                labsListAdapter = new LabsListAdapter(this, labs);
                viewLabs.setAdapter(labsListAdapter);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getLabs() {
        labs = new ArrayList<>();
        Cursor cursor = DbLabs.getLabsBySubjectId(db, subjectId);
        while(cursor.moveToNext()){
            int idLab = cursor.getInt(0);
            int idSub = cursor.getInt(1);
            int LabProtected = cursor.getInt(2);
            LabModel lab = new LabModel(idLab, idSub, LabProtected);
            labs.add(lab);
        }
        cursor.close();
    }

    private void updateLabs(int id, int labProtected) {
        try {
            if (DbLabs.updateLabById(db2, id, labProtected) > 0) {
                getLabs();
                labsListAdapter = new LabsListAdapter(this, labs);
                viewLabs.setAdapter(labsListAdapter);
            } else {
                Toast.makeText(this, "Ошибка 1", Toast.LENGTH_SHORT).show();
            }


            int countLabs = DbLabs.getNumberOfLabsBySubjectId(db, subjectId);
            int countLabsProtected = DbLabs.getNumberOfProtectedLabsBySubjectId(db, subjectId);
            if (countLabs == countLabsProtected) {
                updateSesssionApi("Допущен");
            } else {
                updateSesssionApi("Не допущен");
            }

        } catch (Exception e) {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    public class LabsListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<LabModel> labs;

        public  LabsListAdapter(Context context, ArrayList<LabModel> labs) {
            this.context = context;
            this.labs = labs;
        }

        @Override
        public int getCount() {
            return labs.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup viewGroup) {
            View view = getLayoutInflater().inflate(R.layout.lab_item, null);

            CheckBox checkBox = view.findViewById(R.id.checkBox);
            checkBox.setText("Лабораторная работа №" + (pos+1)); // увеличиваем позицию на единицу для вывода номера, начиная с 1

            boolean isChecked = labs.get(pos).getLabProtected() == 1; // определяем, должна ли быть установлена галочка на основе значения getLabProtected()
            checkBox.setChecked(isChecked); // устанавливаем состояние CheckBox на основе значения isChecked

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    labs.get(pos).setLabProtected(isChecked ? 1 : 0); // устанавливаем значение поля labProtected объекта LabModel на основе состояния CheckBox

                    // вызываем метод updateLabs()
                    if (isChecked) {
                        // если CheckBox отмечен
                        //updateLabs(labs.get(pos).getId(), 1);
                        updateLabApi(labs.get(pos).getId(), 1);
                    } else {
                        // если CheckBox не отмечен
                        //updateLabs(labs.get(pos).getId(), 0);
                        updateLabApi(labs.get(pos).getId(), 0);
                    }
                }
            });

            return view;
        }
    }

    private void updateLabApi(int idPos, int LabProtPos) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://93.125.10.36/Test/api/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegistrationApi registrationApi = retrofit.create(RegistrationApi.class);
        LabModel labModel = new LabModel(idPos, subjectId, LabProtPos);
        registrationApi.updateLab(labModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Обновление данных прошло успешно (статус код 200)
                    updateLabs(idPos, LabProtPos);
                } else {
                    // Обновление данных не удалось
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Обновление данных не удалось
                Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSesssionApi(String newStatus) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://93.125.10.36/Test/api/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegistrationApi registrationApi = retrofit.create(RegistrationApi.class);
        SessionModel sessionModel = null;
        Cursor cursor = DbSession.getSessionBySubjectId(db, subjectId);
        while(cursor.moveToNext()){
            sessionModel = new SessionModel(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), newStatus, cursor.getInt(4), cursor.getString(5), cursor.getString(6));
        }
        cursor.close();

        registrationApi.updateSession(sessionModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Обновление данных прошло успешно (статус код 200)
                    if (DbSession.updateSessionBySubjectId(db2, subjectId, newStatus) > 0) {
                        getLabs();
                        labsListAdapter = new LabsListAdapter(getApplicationContext(), labs);
                        viewLabs.setAdapter(labsListAdapter);
                    } else {
                        Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Обновление данных не удалось
                    Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Обновление данных не удалось
                Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteLabApi(int idDel) {//допил
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://93.125.10.36/Test/api/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegistrationApi registrationApi = retrofit.create(RegistrationApi.class);
        LabModel labModel = new LabModel(38, 17, 0);
        registrationApi.deleteLab(labModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Обновление данных прошло успешно (статус код 200)
                    if(DbLabs.deleteLab(db, idDel) != -1) {
                    } else {
                        Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Обновление данных не удалось
                    Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Обновление данных не удалось
                Log.e("DeleteLabApi", "Failed to delete lab", t);
                Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Subjects.class);
        intent.putExtra("userIdSub", userId);
        startActivity(intent);
    }
}