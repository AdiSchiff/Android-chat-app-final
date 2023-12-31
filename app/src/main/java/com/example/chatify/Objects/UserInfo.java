package com.example.chatify.Objects;

public class UserInfo {
    private String username;
    private String displayName;
    private String profilePic;

    public UserInfo(String username, String displayName, String profilePicture) {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}