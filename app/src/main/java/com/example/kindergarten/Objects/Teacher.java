package com.example.kindergarten.Objects;

import java.util.ArrayList;

public class Teacher {
    private String uid = "";
    private String name = "";
    private String ID = "";
    private String phoneNum = "";
    private int experienceYears = 0;
    private String workPlace = "";
    private int age = 0;
    private String imageURL = "";
    private ArrayList<String> history = new ArrayList<>();

    public Teacher(){}

    public Teacher(String uid, String name, String ID, String phoneNum, int experienceYears, String workPlace, int age, String imageURL, ArrayList<String> chats) {
        this.uid = uid;
        this.name = name;
        this.ID = ID;
        this.phoneNum = phoneNum;
        this.experienceYears = experienceYears;
        this.workPlace = workPlace;
        this.age = age;
        this.imageURL = imageURL;
        this.history = chats;
    }

    public Teacher(String uid, String name, String ID, String phoneNum, String experienceYears, String workPlace, String age, String imageURL){
        this.uid = uid;
        this.name = name;
        this.ID = ID;
        this.phoneNum = phoneNum;
        this.experienceYears = Integer.parseInt(experienceYears);
        this.workPlace = workPlace;
        this.age = Integer.parseInt(age);
        this.imageURL = imageURL;
    }

    public ArrayList<String> getChats() {
        return history;
    }

    public void setChats(ArrayList<String> chats) {
        this.history = chats;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getName() {
        return name;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
