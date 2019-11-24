package com.mongodb.entity;



public class Userlike {
    private String userId;
    private String userAvatar;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    @Override
    public String toString() {
        return "Userlike{" +
                "userId='" + userId + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                '}';
    }
}

