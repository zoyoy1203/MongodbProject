package com.mongodb.entity;

// 评论模板
public class Comment {
    private String userId;
    private String nickname;
    private String content;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
