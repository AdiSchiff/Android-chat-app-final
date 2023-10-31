package com.example.chatify.Objects;

public class ChatsArray {
    private int id;
    private UserInfo userInfo;
    LastMsg lastMsg;

    public ChatsArray(int id, UserInfo userInfo, LastMsg lastMsg) {
        this.id = id;
        this.userInfo = userInfo;
        this.lastMsg = lastMsg;
    }

    public int getId() {
        return this.id;
    }

    public LastMsg getLastMsg() {
        return lastMsg;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

}
