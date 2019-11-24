package com.mongodb.entity;

import org.bson.types.ObjectId;

// 用户动态信息
public class UserInfo {
    private ObjectId _id;
    private String nickname;
    private String avatar;  // 头像地址
    private Info info;

    public UserInfo(ObjectId _id, String nickname, String avatar, Info info) {
        this._id = _id;
        this.nickname = nickname;
        this.avatar = avatar;
        this.info = info;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "_id=" + _id +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", info=" + info +
                '}';
    }
}
