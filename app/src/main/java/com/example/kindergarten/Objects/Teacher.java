package com.example.kindergarten.Objects;

public class Teacher {
    private String uid = "";
    private String name = "";
    private int ID = 0;
    private String phoneNum = "";
    private int experienceYears = 0;
    private String workPlace = "";
    private int age = 0;
    private String imageURL = "";

    public Teacher(){}

    public Teacher(String uid,String name, String phoneNum, String experienceYear, String workPlace, String age, String imageURL){
        this.uid = uid;
        this.name = name;
        this.phoneNum = phoneNum;
        this.experienceYears = Integer.parseInt(experienceYear);
        this.workPlace = workPlace;
        this.age = Integer.parseInt(age);
        this.imageURL = imageURL;
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

    public int getExperienceYear() {
        return experienceYears;
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

    public void setExperienceYear(int experienceYear) {
        this.experienceYears = experienceYear;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
