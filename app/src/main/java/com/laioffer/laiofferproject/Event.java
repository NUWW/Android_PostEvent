package com.laioffer.laiofferproject;

/**
 * Created by weiguang on 8/27/17.
 */

public class Event {
//    private String title;
//    private String address;
//    private String description;
//
//    public Event(String title, String address, String descreption) {
//        this.title = title;
//        this.address = address;
//        this.description = descreption;
//    }
//
//    public String getTitle() {
//        return this.title;
//    }
//
//    public String getAddress() {
//        return this.address;
//    }
//
//    public String getDescription() {
//        return this.description;
//    }
    private int good;
    private int bad;
    private int commendNumber;
    private String id;
    private String location;
    private long time;
    private String username;
    private String description;
    private int repost;
    private String title;
    private String imgUri;

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public int getRepost() {
        return repost;
    }

    public void setRepost(int repost) {
        this.repost = repost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getBad() {
        return bad;
    }

    public void setBad(int bad) {
        this.bad = bad;
    }

    public int getCommendNumber() {
        return commendNumber;
    }

    public void setCommendNumber(int commendNumber) {
        this.commendNumber = commendNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
