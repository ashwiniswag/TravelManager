package com.example.travelmanager;

public class User {

    public String DisplayName,UserName,Bio;

    public User(String DisplayName,String UserName,String Bio) {
        this.DisplayName=DisplayName;
        this.UserName=UserName;
        this.Bio=Bio;
    }

    public User(String DisplayName,String UserName){
        this.DisplayName=DisplayName;
        this.UserName=UserName;
        Bio="";
    }
}
