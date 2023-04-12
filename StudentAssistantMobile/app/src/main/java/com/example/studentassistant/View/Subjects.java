package com.example.studentassistant.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentassistant.DataBase.DbHelper;
import com.example.studentassistant.DataBase.DbLabs;
import com.example.studentassistant.DataBase.DbSession;
import com.example.studentassistant.DataBase.DbSubjects;
import com.example.studentassistant.DataBase.DbUser;
import com.example.studentassistant.Model.LabModel;
import com.example.studentassistant.Model.SessionModel;
import com.example.studentassistant.Model.SubjectModel;
import com.example.studentassistant.Model.UserModel;
import com.example.studentassistant.R;

import java.util.ArrayList;

public class Subjects extends AppCompatActivity {
    private int userId;
    private String sessionType;
    private SQLiteDatabase db;
    private ArrayList<SubjectModel> subjects;
    private SubjectsListAdapter subjectsListAdapter;
    private ListView viewSubjects;
    private SubjectItemContextMenu subjectItemContextMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        setTitle("Предметы");

        Intent intent = getIntent();
        userId = (Integer) intent.getSerializableExtra("userIdSub");

        bind();
        updateAdapter();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_subjects, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.btAddSab:
                showAddSubjectDialog(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void bind() {
        db = new DbHelper(getApplicationContext()).getReadableDatabase();
        viewSubjects = findViewById(R.id.listViewSubjects);
        sessionType = "-";
    }

    public interface SubjectItemContextMenu {
        void onDelete(int position);
        void onEdit(int position);
    }

    public void onRadioButtonClicked(View view) {
        // если переключатель отмечен
        boolean checked = ((RadioButton) view).isChecked();
        // Получаем нажатый переключатель
        switch(view.getId()) {
            case R.id.radioButtonExam:
                if (checked){
                    sessionType = "Экзамен";
                }
                break;
            case R.id.radioButtonOffset:
                if (checked){
                    sessionType = "Зачёт";
                }
                break;
        }
    }

    private void showAddSubjectDialog(Context context) {
        // Создание пользовательского макета
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_add_subject, null);

        EditText edit_text_subject_name = dialogView.findViewById(R.id.edit_text_subject_name);
        EditText edit_text_subject_count = dialogView.findViewById(R.id.edit_text_subject_count);

        // Создание диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Добавление нового предмета")
                .setView(dialogView)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sessionType.equals("-")) {
                            Toast.makeText(context, "Выберите форму сдачи", Toast.LENGTH_SHORT).show();
                        } else {
                        String subjectName = edit_text_subject_name.getText().toString();
                        String subjectCount = edit_text_subject_count.getText().toString();
                        int count = 0;
                        try {
                            count = Integer.parseInt(subjectCount);
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Введите корректные данные", Toast.LENGTH_SHORT).show();
                        }
                        //добавление предмета в базу данных
                        if (count < 1 || subjectName.trim().equals("")) {
                            Toast.makeText(context, "Введите корректные данные", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                SubjectModel subjectModel = new SubjectModel(userId, subjectName);
                                if (DbSubjects.add(db, subjectModel) != -1) {
                                    Toast.makeText(context, "Предмет добавлен: " + subjectName, Toast.LENGTH_SHORT).show();
                                    updateAdapter();
                                } else
                                    Toast.makeText(context, "Ошибка добавления. Возможно такой предмет уже существует", Toast.LENGTH_SHORT).show();

                                LabModel labModel = new LabModel(getSubjectIdBySubjectName(subjectName), 0);
                                for (int i = 0; i < count; i++) {
                                    if (DbLabs.add(db, labModel) != -1) {
                                    } else
                                        Toast.makeText(context, "Ошибка добавления", Toast.LENGTH_SHORT).show();
                                }

                                SessionModel sessionModel = new SessionModel(getSubjectIdBySubjectName(subjectName), sessionType, "Не допущен");
                                if (DbSession.add(db, sessionModel) != -1) {
                                } else
                                    Toast.makeText(context, "Ошибка добавления", Toast.LENGTH_SHORT).show();


                            } catch (Exception e) {
                                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
                            }
                        }
                        }
                    }

                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Отображение диалогового окна
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showUpdateSubjectDialog(Context context, int id) {
        // Создание пользовательского макета
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_update_subject, null);
        EditText edit_text_subject_name = dialogView.findViewById(R.id.edit_text_subject_name_update);

        // Создание диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Изменение предмета")
                .setView(dialogView)
                .setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String subjectName = edit_text_subject_name.getText().toString();
                        //изменение предмета в базе данных
                        try {
                            if(DbSubjects.updateSubjectById(db, id, subjectName) != -1) {
                                Toast.makeText(context, "Изменено на: " + subjectName, Toast.LENGTH_SHORT).show();
                                updateAdapter();
                            } else Toast.makeText(context, "Ошибка изменения", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Отображение диалогового окна
        AlertDialog dialog = builder.create();
        dialog.show();
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

    private void updateAdapter() {
        getSubjects();
        subjectsListAdapter = new SubjectsListAdapter(this, subjects, new SubjectItemContextMenu() {
            @Override
            public void onDelete(int id) {
                try {
                    if (DbSubjects.deleteSubject(db, id) > 0) {
                        updateAdapter();
                    } else {
                        Toast.makeText(Subjects.this, "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Subjects.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onEdit(int id) {
                showUpdateSubjectDialog(Subjects.this, id);
            }
        });
        viewSubjects.setAdapter(subjectsListAdapter);
    }

    private int getSubjectIdBySubjectName(String name) {
        int idSubject = 0;
        Cursor cursor = DbSubjects.getSubjectByName(db, name);
        while(cursor.moveToNext()){
            idSubject = cursor.getInt(0);
        }
        cursor.close();
        return idSubject;
    }

    public class SubjectsListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<SubjectModel> subjects;
        private SubjectItemContextMenu subjectItemContextMenu;

        public  SubjectsListAdapter(Context context, ArrayList<SubjectModel> subjects, SubjectItemContextMenu subjectItemContextMenu) {
            this.context = context;
            this.subjects = subjects;
            this.subjectItemContextMenu = subjectItemContextMenu;
        }

        @Override
        public int getCount() {
            return subjects.size();
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
            View view = getLayoutInflater().inflate(R.layout.subject_item, null);

            TextView subjectName = view.findViewById(R.id.txtSubjectName);
            TextView subjectInfo = view.findViewById(R.id.txtSubjectInfo);//В разработке

            subjectName.setText(subjects.get(pos).getSubjectName());

            int countLabs = DbLabs.getNumberOfLabsBySubjectId(db, subjects.get(pos).getId());
            int countLabsProtected = DbLabs.getNumberOfProtectedLabsBySubjectId(db, subjects.get(pos).getId());
            subjectInfo.setText(countLabsProtected + "/" + countLabs);

            view.setOnClickListener(v -> {
                Intent intent = new Intent(Subjects.this, Labs.class);
                intent.putExtra("subjectIdLab", subjects.get(pos).getId());
                intent.putExtra("subjectNameLab", subjects.get(pos).getSubjectName());
                intent.putExtra("userIdIntent",userId);
                startActivity(intent);
            });


            view.setOnLongClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.subject_item_context_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.delete:
                            subjectItemContextMenu.onDelete(subjects.get(pos).getId());
                            return true;
                        case R.id.edit:
                            subjectItemContextMenu.onEdit(subjects.get(pos).getId());
                            return true;
                        default:
                            return false;
                    }
                });

                popupMenu.show();
                return true;
            });

            return view;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slide_sub_out, R.anim.slide_sub_in);
    }
}