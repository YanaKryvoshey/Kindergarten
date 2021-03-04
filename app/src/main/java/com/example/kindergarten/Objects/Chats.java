package com.example.kindergarten.Objects;

import java.util.ArrayList;

public class Chats {
    private int change;
    private ArrayList<String> message = new ArrayList<>();

    public Chats(){}

    public Chats(int change, ArrayList<String> message) {
        this.change = change;
        this.message = message;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
    }
}
