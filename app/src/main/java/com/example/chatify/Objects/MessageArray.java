package com.example.chatify.Objects;

import java.util.Date;

public class MessageArray {
    private int id;
    private Date created;
    private  UserInfo userInfo;
    private String content;

    public MessageArray(int id, Date created, UserInfo userInfo, String content) {
        this.id = id;
        this.created = created;
        this.userInfo = userInfo;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getContent() {
        return content;
    }
}
