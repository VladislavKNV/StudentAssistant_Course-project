package com.example.studentassistant.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.example.studentassistant.Model.LabModel;
import com.example.studentassistant.Model.SubjectModel;
import com.example.studentassistant.R;

import java.util.ArrayList;

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

                LabModel labModel = new LabModel(subjectId, 0);
                if(DbLabs.add(db, labModel) != -1) {
                    getLabs();
                    labsListAdapter = new LabsListAdapter(this, labs);
                    viewLabs.setAdapter(labsListAdapter);
                } else Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.btDelLab:
                int count = labs.size() - 1;
                if(DbLabs.deleteLab(db, labs.get(count).getId()) != -1) {
                    getLabs();
                    labsListAdapter = new LabsListAdapter(this, labs);
                    viewLabs.setAdapter(labsListAdapter);
                } else Toast.makeText(this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
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
                if (DbSession.updateSessionBySubjectId(db2, subjectId, "Допущен") > 0) {
                    getLabs();
                    labsListAdapter = new LabsListAdapter(this, labs);
                    viewLabs.setAdapter(labsListAdapter);
                } else {
                    Toast.makeText(this, "Ошибка 2", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (DbSession.updateSessionBySubjectId(db2, subjectId, "Не допущен") > 0) {
                    getLabs();
                    labsListAdapter = new LabsListAdapter(this, labs);
                    viewLabs.setAdapter(labsListAdapter);
                } else {
                    Toast.makeText(this, "Ошибка 2", Toast.LENGTH_SHORT).show();
                }
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

                    // вызываем ваш метод updateLabs()
                    if (isChecked) {
                        // если CheckBox отмечен
                        updateLabs(labs.get(pos).getId(), 1);
                    } else {
                        // если CheckBox не отмечен
                        updateLabs(labs.get(pos).getId(), 0);
                    }
                }
            });

            return view;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Subjects.class);
        intent.putExtra("userIdSub", userId);
        startActivity(intent);
    }
}