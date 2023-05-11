package com.example.studentassistant.Model;

//public class UserModel {
//    private int id;
//    private int id_role;
//    private String login;
//    private String email;
//    private String password;
//
//    public UserModel(int id_role, String login, String email, String password) {
//        this.id_role = id_role;
//        this.login = login;
//        this.email = email;
//        this.password = password;
//    }
//    public UserModel(int id, int id_role, String login, String email, String password) {
//        this.id = id_role;
//        this.id_role = id_role;
//        this.login = login;
//        this.email = email;
//        this.password = password;
//    }
//
//    public UserModel(String login, String password) {
//        this.login = login;
//        this.password = password;
//    }
//
//    public int getId() {return id;}
//    public int getIdRole() {return id_role;}
//    public String getLogin() {return login;}
//    public String getEmail() {return email;}
//    public String getPassword() {return password;}
//
//
//    public void setId(int id) {this.id = id;}
//    public void setIdRole(int id_role) {this.id_role = id_role;}
//    public void setLogin(String login) {this.login = login;}
//    public void setEmail(String email){this.email = email;}
//    public void setPassword(String password){this.password = password;}
//}
public class UserModel {
    private int id;
    private int RoleId;
    private String Login;
    private String Email;
    private String Password;

    public UserModel(int roleId, String login, String email, String password) {
        RoleId = roleId;
        Login = login;
        Email = email;
        Password = password;
    }
    public UserModel(int id_n, int roleId, String login, String email, String password) {
        id = id_n;
        RoleId = roleId;
        Login = login;
        Email = email;
        Password = password;
    }

    public UserModel(String login, String password) {
        Login = login;
        Password = password;
    }

    public UserModel(int id_n) {
        id = id_n;
    }

    public int getId() {
        return id;
    }
    public int getRoleId() {
        return RoleId;
    }

    public String getLogin() {
        return Login;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setId(int id) {
        this.id = id;
    }
}

