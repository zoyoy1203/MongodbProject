package com.mongodb.entity;

import org.bson.types.ObjectId;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

// 动态模板
public class Info {
    private ObjectId _id;
    private String text;
    private List<String> images;
    private List<Userlike> like;
    private List<String> likeId;
    private List<Comment> comment;
    private Date createDate;

    public Info(ObjectId _id, String text, List<String> images, List<Userlike> like, List<String> likeId, List<Comment> comment) {
        this._id = _id;
        this.text = text;
        this.images = images;
        this.like = like;
        this.likeId = likeId;
        this.comment = comment;
        this.createDate = new Date();
    }


    public Info(String text, List<String> images) {
        this._id = new ObjectId();
        this.text = text;
        this.images = images;
        this.createDate = new Date();
    }

    public Info(String text) {
        this._id = new ObjectId();
        this.text = text;
        this.createDate = new Date();
    }

    public Info() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Userlike> getLike() {
        return like;
    }

    public void setLike(List<Userlike> like) {
        this.like = like;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Info{" +
                "_id=" + _id +
                ", text='" + text + '\'' +
                ", images=" + images +
                ", like=" + like +
                ", comment=" + comment +
                ", createDate=" + createDate +
                '}';
    }
}
