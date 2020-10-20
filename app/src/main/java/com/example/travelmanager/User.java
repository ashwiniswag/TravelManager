package com.example.travelmanager;

public class User {

    public String DisplayName,uname,Bio;

    public User(String DisplayName,String UserName,String Bio) {
        this.DisplayName=DisplayName;
        this.uname=uname;
        this.Bio=Bio;
    }

    public User(String DisplayName,String UserName){
        this.DisplayName=DisplayName;
        this.uname=UserName;
        Bio="";
    }
}
