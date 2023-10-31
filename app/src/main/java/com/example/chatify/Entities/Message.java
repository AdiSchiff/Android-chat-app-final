package com.example.chatify.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.chatify.Adapters.UserInfoConverter;
import com.example.chatify.Objects.UserInfo;

@Entity(tableName = "messages")
public class Message {



    @PrimaryKey(autoGenerate = true)
    private int generatedID;
    @NonNull
    private int contactId;
    private String username;
    private String content;
    private String timeSent;
    private String displayName;
    private String profilePic;

    public Message(int contactId, String content, String timeSent, UserInfo sender) {
        this.contactId = contactId;
        this.content = content;
        this.timeSent = timeSent;
        this.username = sender.getUsername();
        this.displayName = sender.getDisplayName();
        this.profilePic = sender.getProfilePic();
    }

    public Message() {
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
    public int getGeneratedID() {
        return generatedID;
    }

    public void setGeneratedID(int generatedID) {
        this.generatedID = generatedID;
    }
}
