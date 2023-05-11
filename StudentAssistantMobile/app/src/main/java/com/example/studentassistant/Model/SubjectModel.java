package com.example.studentassistant.Model;

public class SubjectModel {
    private int SubjectId;
    private int UserId;
    private String Name;

    public SubjectModel(int id) {
        this.SubjectId = id;
    }
    public SubjectModel(int id_user, String subjectName) {
        this.UserId = id_user;
        this.Name = subjectName;
    }

    public SubjectModel(int id, int id_user, String subjectName) {
        this.SubjectId = id;
        this.UserId = id_user;
        this.Name = subjectName;
    }


    public int getId() {return SubjectId;}
    public int getIdUser() {return UserId;}
    public String getSubjectName() {return Name;}

    public void setId(int id) {this.SubjectId = id;}
    public void setIdUser(int id_user) {this.UserId = id_user;}
    public void setSubjectName(String subjectName) {this.Name = subjectName;}
}
