package com.example.travelmanager;

import android.graphics.Bitmap;

public class ModdleClass {

    private Bitmap tripphoto,user_image;
    private int likesignal;
    private String caption,likes,comment,username;

    public ModdleClass() {

    }

    public ModdleClass(Bitmap tripphoto, Bitmap user_image, int likesignal, String caption, String likes, String comment,String username) {
        this.tripphoto = tripphoto;
        this.user_image = user_image;
        this.likesignal = likesignal;
        this.caption = caption;
        this.likes = likes;
        this.comment = comment;
        this.username=username;
    }

    public Bitmap getTripphoto() {
        return tripphoto;
    }

    public Bitmap getUser_image() {
        return user_image;
    }

    public int getLikesignal() {
        return likesignal;
    }

    public String getCaption() {
        return caption;
    }

    public String getLikes() {
        return likes;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }
}
