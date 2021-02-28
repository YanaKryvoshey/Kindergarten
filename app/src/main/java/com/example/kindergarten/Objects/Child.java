package com.example.kindergarten.Objects;

public class Child {

    private String uid = "";
    private String name = "";
    private int ID = 0;
    private String phoneNum1 = "";
    private String phoneNum2 = "";
    private String birthDay = "";
    private String gardenName = "";

    public Child() {
    }

    public Child(String uid, String name, String ID, String phoneNum1, String phoneNum2, String birthDay, String gardenName) {
        this.uid = uid;
        this.name = name;
        this.ID =  Integer.parseInt(ID);
        this.phoneNum1 = phoneNum1;
        this.phoneNum2 = phoneNum2;
        this.birthDay = birthDay;
        this.gardenName = gardenName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGardenName() {
        return gardenName;
    }

    public void setGardenName(String gardenName) {
        this.gardenName = gardenName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getPhoneNum1() {
        return phoneNum1;
    }

    public void setPhoneNum1(String phoneNum1) {
        this.phoneNum1 = phoneNum1;
    }

    public String getPhoneNum2() {
        return phoneNum2;
    }

    public void setPhoneNum2(String phoneNum2) {
        this.phoneNum2 = phoneNum2;
    }
}
