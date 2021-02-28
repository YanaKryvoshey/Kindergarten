package com.example.kindergarten.Objects;

import java.util.ArrayList;

public class Garden {

        private String name = "";
        private String phoneNum = "";
        private double latitude= 0;
        private double longitude = 0;
        private int numberOfTeachers = 0;
        private int maxChildren = 0;
        private int numOfChildren = 0;
        private int numberAvailablePlaces = 0;
        private ArrayList<String> allpic = new ArrayList<>();


        public Garden(){}

        public Garden( String name, String phoneNum, double latitude, double longitude, String numberOfTeachers, String maxChildren, String numOfChildren,ArrayList<String> allpic) {

                this.name = name;
                this.phoneNum = phoneNum;
                this.latitude = latitude;
                this.longitude = longitude;
                this.maxChildren = Integer.parseInt(maxChildren);
                this.numberOfTeachers = Integer.parseInt(numberOfTeachers);
                this.numOfChildren = Integer.parseInt(numOfChildren);
                this.numberAvailablePlaces =Integer.parseInt(maxChildren) - Integer.parseInt(numOfChildren);
                this.allpic = allpic;
        }


        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getPhoneNum() {
                return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
                this.phoneNum = phoneNum;
        }

        public double getLatitude() {
                return latitude;
        }

        public void setLatitude(double latitude) {
                this.latitude = latitude;
        }

        public double getLongitude() {
                return longitude;
        }

        public void setLongitude(double longitude) {
                this.longitude = longitude;
        }

        public int getNumberOfTeachers() {
                return numberOfTeachers;
        }

        public void setNumberOfTeachers(int numberOfTeachers) {
                this.numberOfTeachers = numberOfTeachers;
        }

        public int getMaxChildren() {
                return maxChildren;
        }

        public void setMaxChildren(int maxChildren) {
                this.maxChildren = maxChildren;
        }

        public int getNumOfChildren() {
                return numOfChildren;
        }

        public void setNumOfChildren(int numOfChildren) {
                this.numOfChildren = numOfChildren;
        }

        public int getNumberAvailablePlaces() {
                return numberAvailablePlaces;
        }

        public void setNumberAvailablePlaces(int numberAvailablePlaces) {
                this.numberAvailablePlaces = numberAvailablePlaces;
        }

        public ArrayList<String> getAllpic() {
                return allpic;
        }

        public void setAllpic(ArrayList<String> allpic) {
                this.allpic = allpic;
        }
}
