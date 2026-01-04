package com.example.wordapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

public class Account {
    private String account_id;
    @JsonIgnore
    private String password;
    private final HashMap<String, String> wordDic = new HashMap<String, String>();

    public Account(String aid, String pass) {
        account_id = aid;
        password = pass;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public void setPassword(String p) {
        password = p;
    }

    public String getPassword() {
        return password;
    }

    public HashMap<String, String> getWords() {
        return wordDic;
    }

}