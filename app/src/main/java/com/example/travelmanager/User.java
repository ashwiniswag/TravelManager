package com.example.travelmanager;

public class User {

    String dname,uname,description;

    public User(String dname,String uname,String description) {
        this.dname=dname;
        this.uname=uname;
        this.description=description;
    }

    public User(String dname,String uname){
        this.dname=dname;
        this.uname=uname;
    }
}
