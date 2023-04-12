package com.example.studentassistant.Model;

public class SubjectModel {
    private int id;
    private int id_user;
    private String subjectName;

    public SubjectModel(int id_user, String subjectName) {
        this.id_user = id_user;
        this.subjectName = subjectName;
    }

    public SubjectModel(int id, int id_user, String subjectName) {
        this.id = id;
        this.id_user = id_user;
        this.subjectName = subjectName;
    }



    public int getId() {return id;}
    public int getIdUser() {return id_user;}
    public String getSubjectName() {return subjectName;}

    public void setId(int id) {this.id = id;}
    public void setIdUser(int id_user) {this.id_user = id_user;}
    public void setSubjectName(String subjectName) {this.subjectName = subjectName;}
}
