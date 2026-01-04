package com.example.wordapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

public class Account {
    private String accountId;
    @JsonIgnore
    private String password;
    private final HashMap<String, String> wordDic = new HashMap<String, String>();

    public Account(String aid, String pass) {
        accountId = aid;
        password = pass;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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