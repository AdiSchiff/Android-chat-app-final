package com.example.chatify.Objects;

public class ContactNoMsg {
    private int id;

    private UserInfo userInfo;

    public ContactNoMsg(int id, UserInfo userInfo) {
        this.id = id;
        this.userInfo = userInfo;
    }

    public int getId() {
        return this.id;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

}