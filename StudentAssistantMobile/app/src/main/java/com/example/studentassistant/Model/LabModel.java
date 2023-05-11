package com.example.studentassistant.Model;

public class LabModel {
    private int LabsId;
    private int SubjectsId;
    private int LabProtected;

    public LabModel(int id) {
        this.LabsId = id;
    }

    public LabModel(int id_subject, int labProtected) {
        this.SubjectsId = id_subject;
        this.LabProtected = labProtected;
    }

    public LabModel( int id, int id_subject, int labProtected) {
        this.LabsId = id;
        this.SubjectsId = id_subject;
        this.LabProtected = labProtected;
    }


    public int getId() {return LabsId;}
    public int getIdSubject() {return SubjectsId;}
    public int getLabProtected() {return LabProtected;}

    public void setId(int id) {this.LabsId = id;}
    public void setIdSubject(int id_subject) {this.SubjectsId = id_subject;}
    public void setLabProtected(int labProtected) {this.LabProtected = labProtected;}
}

