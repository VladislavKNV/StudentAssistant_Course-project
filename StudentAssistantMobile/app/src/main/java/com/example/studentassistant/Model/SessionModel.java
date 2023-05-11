package com.example.studentassistant.Model;

public class SessionModel {
    private int SessionId;
    private int SubjectId;
    private String Type;
    private String Status;
    private int Mark;
    private String DateTime;
    private String Auditorium;

    public SessionModel(int id_subject, String type, String status, int mark, String dateTime, String auditorium) {
        this.SubjectId = id_subject;
        this.Type = type;
        this.Status = status;
        this.Mark = mark;
        this.DateTime = dateTime;
        this.Auditorium = auditorium;
    }

    public SessionModel( int id, int id_subject, String type, String status, int mark, String dateTime, String auditorium) {
        this.SessionId = id;
        this.SubjectId = id_subject;
        this.Type = type;
        this.Status = status;
        this.Mark = mark;
        this.DateTime = dateTime;
        this.Auditorium = auditorium;
    }

    public SessionModel(int id_subject, String type, String status) {
        this.SubjectId = id_subject;
        this.Type = type;
        this.Status = status;
    }

    public int getId() {return SessionId;}
    public int getIdSubject() {return SubjectId;}
    public String getType() {return Type;}
    public String getStatus() {return Status;}
    public int getMark() {return Mark;}
    public String getDateTime() {return DateTime;}
    public String getAuditorium() {return Auditorium;}


    public void setId(int id) {this.SessionId = id;}
    public void setIdSubject(int id_subject) {this.SubjectId = id_subject;}
    public void setType(String type) {this.Type = type;}
    public void setStatus(String status){this.Status = status;}
    public void setMark(int mark){this.Mark = mark;}
    public void setDateTime(String dateTime){this.DateTime = dateTime;}
    public void setAuditorium(String auditorium){this.Auditorium = auditorium;}
}
