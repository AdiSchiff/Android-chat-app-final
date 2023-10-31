package com.example.chatify.Adapters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.example.chatify.Objects.UserInfo;

public class UserInfoConverter {
    private Gson gson = new Gson();

    @TypeConverter
    public String fromUserInfo(UserInfo userInfo) {
        return gson.toJson(userInfo);
    }

    @TypeConverter
    public UserInfo toUserInfo(String userInfoJson) {
        return gson.fromJson(userInfoJson, UserInfo.class);
    }
}

