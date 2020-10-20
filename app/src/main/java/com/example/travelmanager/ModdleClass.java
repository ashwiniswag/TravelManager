package com.example.travelmanager;

public class ModdleClass {

    private int tripphoto,user_image,likesignal;
    private String caption,likes,comment;

    public ModdleClass() {

    }

    public ModdleClass(int tripphoto, int user_image, int likesignal, String caption, String likes, String comment) {
        this.tripphoto = tripphoto;
        this.user_image = user_image;
        this.likesignal = likesignal;
        this.caption = caption;
        this.likes = likes;
        this.comment = comment;
    }

    public int getTripphoto() {
        return tripphoto;
    }

    public int getUser_image() {
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
}
