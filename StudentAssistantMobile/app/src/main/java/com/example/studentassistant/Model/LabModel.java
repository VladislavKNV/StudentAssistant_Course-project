package com.example.studentassistant.Model;

public class LabModel {
    private int id;
    private int id_subject;
    private int labProtected;

    public LabModel(int id_subject, int labProtected) {
        this.id_subject = id_subject;
        this.labProtected = labProtected;
    }

    public LabModel( int id, int id_subject, int labProtected) {
        this.id = id;
        this.id_subject = id_subject;
        this.labProtected = labProtected;
    }


    public int getId() {return id;}
    public int getIdSubject() {return id_subject;}
    public int getLabProtected() {return labProtected;}

    public void setId(int id) {this.id = id;}
    public void setIdSubject(int id_subject) {this.id_subject = id_subject;}
    public void setLabProtected(int labProtected) {this.labProtected = labProtected;}
}

