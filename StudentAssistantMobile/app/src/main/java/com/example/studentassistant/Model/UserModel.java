package com.example.studentassistant.Model;

public class UserModel {
    private int id;
    private int id_role;
    private String login;
    private String email;
    private String password;

    public UserModel(int id_role, String login, String email, String password) {
        this.id_role = id_role;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public int getId() {return id;}
    public int getIdRole() {return id_role;}
    public String getLogin() {return login;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}


    public void setId(int id) {this.id = id;}
    public void setIdRole(int id_role) {this.id_role = id_role;}
    public void setLogin(String login) {this.login = login;}
    public void setEmail(String email){this.email = email;}
    public void setPassword(String password){this.password = password;}
}
