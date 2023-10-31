package com.example.chatify.Adapters;

import androidx.room.TypeConverter;

import com.example.chatify.Objects.LastMsg;
import com.example.chatify.Objects.UserInfo;
import com.google.gson.Gson;

public class LastMsgConverter {

    @TypeConverter
    public static LastMsg fromString(String value) {
        return new Gson().fromJson(value, LastMsg.class);
    }

    @TypeConverter
    public static String toString(LastMsg lastMsg) {
        return new Gson().toJson(lastMsg);
    }
}
