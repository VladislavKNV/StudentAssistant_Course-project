package com.example.studentassistant.Model;

public class SessionModel {
    private int id;
    private int id_subject;
    private String type;
    private String status;
    private int mark;
    private String dateTime;
    private String auditorium;

    public SessionModel(int id_subject, String type, String status, int mark, String dateTime, String auditorium) {
        this.id_subject = id_subject;
        this.type = type;
        this.status = status;
        this.mark = mark;
        this.dateTime = dateTime;
        this.auditorium = auditorium;
    }

    public SessionModel(int id_subject, String type, String status) {
        this.id_subject = id_subject;
        this.type = type;
        this.status = status;
    }

    public int getId() {return id;}
    public int getIdSubject() {return id_subject;}
    public String getType() {return type;}
    public String getStatus() {return status;}
    public int getMark() {return mark;}
    public String getDateTime() {return dateTime;}
    public String getAuditorium() {return auditorium;}


    public void setId(int id) {this.id = id;}
    public void setIdSubject(int id_subject) {this.id_subject = id_subject;}
    public void setType(String type) {this.type = type;}
    public void setStatus(String status){this.status = status;}
    public void setMark(int mark){this.mark = mark;}
    public void setDateTime(String dateTime){this.dateTime = dateTime;}
    public void setAuditorium(String auditorium){this.auditorium = auditorium;}
}
