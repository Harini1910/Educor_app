package com.example.educor_app.Notifications;

public class Notification_data {
    private String message,name,time,imgUrl,isSeen,refkey;


    public Notification_data() {

    }

    public Notification_data(String message, String name, String time, String imgUrl,String isSeen,String refkey) {
        this.message = message;
        this.name = name;
        this.time = time;
        this.imgUrl = imgUrl;
        this.isSeen=isSeen;
        this.refkey=refkey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }

    public String getRefkey() {
        return refkey;
    }

    public void setRefkey(String refkey) {
        this.refkey = refkey;
    }
}
