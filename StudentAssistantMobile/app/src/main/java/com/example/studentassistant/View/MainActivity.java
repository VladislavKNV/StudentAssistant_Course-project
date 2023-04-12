package com.example.studentassistant.View;

import static java.lang.Double.NaN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentassistant.DataBase.DbHelper;
import com.example.studentassistant.DataBase.DbLabs;
import com.example.studentassistant.DataBase.DbSession;
import com.example.studentassistant.DataBase.DbSubjects;
import com.example.studentassistant.DataBase.DbUser;
import com.example.studentassistant.Model.SubjectModel;
import com.example.studentassistant.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private SQLiteDatabase db2;
    private String login;
    private int userId;
    public TextView tvLogin;
    public TextView tvSubjects;
    private ArrayList<SubjectModel> subjects;


    private List<Pair<String, String>> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DbHelper(getApplicationContext()).getReadableDatabase();
        db2 = new DbHelper(getApplicationContext()).getWritableDatabase();

        Cursor cursor = DbUser.getAll(db);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count > 0) {
                userId = cursor.getInt(0);
                login = cursor.getString(2);
                setContentView(R.layout.activity_main);
            } else {
                Intent intent = new Intent(this, Authorization.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(this, Authorization.class);
            startActivity(intent);
        }

        bind();

        getSubjects();
        info();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.btSignOut:
                try {
                    if (DbUser.deleteUser(db2, userId) > 0) {
                        Intent intent = new Intent(this, Authorization.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bind() {
        tvLogin = findViewById(R.id.tvLogin);
        tvSubjects = findViewById(R.id.textView2);
    }

    public void btnSubjects (View view) {
        Intent intent = new Intent(this, Subjects.class);
        intent.putExtra("userIdSub", userId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }

    private void info() {
        tvLogin.setText(login);
        int countAll = 0;
        int countProtected = 0;
        int n = 0;
        // Создайте список данных
        mData = new ArrayList<>();
        for (SubjectModel subjectModel : subjects) {
            if (n == 0) {
                mData.add(new Pair<>("Предметы: ", null));
                n++;
            }
            int countLabs = DbLabs.getNumberOfLabsBySubjectId(db,subjectModel.getId());
            int countLabsProtected = DbLabs.getNumberOfProtectedLabsBySubjectId(db, subjectModel.getId());
            countAll += countLabs;
            countProtected += countLabsProtected;
            mData.add(new Pair<>("  \u25CB " + subjectModel.getSubjectName(), countLabsProtected + "/" + countLabs));

        }
        double percent = ((double)countProtected / (double)countAll) * 100;
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedPercent = df.format(percent);
        if (countProtected == 0 && countAll == 0) {

        } else {mData.add(new Pair<>("Прогресс: "+ formattedPercent + "%", countProtected + "/" + countAll));}
        mData.add(new Pair<>("Количество экзаменов: ", "  " + Integer.toString(DbSession.getCountOfExams(db))));
        mData.add(new Pair<>("Количество зачётов: ", "  " + Integer.toString(DbSession.getCountOfOffset(db))));

        if (DbSession.getCountStatus(db) == 0 && countAll > 0) {
            mData.add(new Pair<>("Допуск к сессии:  ", "Есть"));
        } else { mData.add(new Pair<>("Допуск к сессии:  ", "Нет"));}
        String formattedMark = df.format(DbSession.getAverageMark(db));
        mData.add(new Pair<>("Средний балл: ", "  " + formattedMark));


        // Найдите RecyclerView в макете и настройте его
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Создайте и установите адаптер для RecyclerView
        MyAdapter adapter = new MyAdapter(mData);
        recyclerView.setAdapter(adapter);

    }

    private void getSubjects() {
        subjects = new ArrayList<>();
        Cursor cursor = DbSubjects.getUserSubjectsByIdUser(db, userId);
        while(cursor.moveToNext()){
            int idSubject = cursor.getInt(0);
            String Name = cursor.getString(2);
            SubjectModel subject = new SubjectModel(idSubject, userId, Name);
            subjects.add(subject);
        }
        cursor.close();
    }



    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Pair<String, String>> mData;

        public MyAdapter(List<Pair<String, String>> data) {
            mData = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subject_item_table, parent, false);
            return new RecyclerView.ViewHolder(itemView) {};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Pair<String, String> item = mData.get(position);
            TextView textView1 = holder.itemView.findViewById(R.id.textView1);
            TextView textView2 = holder.itemView.findViewById(R.id.textView2);
            textView1.setText(item.first);
            textView2.setText(item.second);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    @Override
    public void onBackPressed() {
        // Здесь ничего не делаем
    }

}