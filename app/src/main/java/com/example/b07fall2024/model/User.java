package com.example.b07fall2024.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable{
    public String username;
    public String email;
    public String uid;
    public boolean isQuestionsCompleted;

    public Map<String, String> answers;

    public User() {
        this.answers = new HashMap<>();
        this.isQuestionsCompleted = false;//有改动！！！
    }

    public User(String username, String email, String uid, boolean isQuestionsCompleted) {
        this.username = username;
        this.email = email;
        this.uid = uid;//do we need password in the realtime database?
        this.isQuestionsCompleted = false;
        this.answers = new HashMap<>();
    }

    public boolean getIsQuestionsCompleted() {
        return isQuestionsCompleted;
    }

    public void setIsQuestionsCompleted(boolean isQuestionsCompleted) {
        this.isQuestionsCompleted = isQuestionsCompleted;
    }

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String email) {
//        this.username = username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getUid() {
//        return uid;
//    }
//
//    public void setUid(String password) {
//        this.uid = uid;
//    }
}
