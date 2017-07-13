package com.easytravelapp17.jodoh;

public class UserList {

    private  int userId;
    private String user_name;
    private String full_name;

    public void initialDataUserList(String user_name, String full_name) {
        this.user_name = user_name;
        this.full_name = full_name;
    }

    public int getUserId() {
        return userId;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
