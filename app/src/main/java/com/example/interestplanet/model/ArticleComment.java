package com.example.interestplanet.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ArticleComment extends AbstractModel {
    private String userId;
    @Exclude
    private User user;
    private String articleId;
    private String comment;
    private String date;

    public ArticleComment() {
    }

    public ArticleComment(String userId, String articleId, String comment, String date) {
        this.userId = userId;
        this.articleId = articleId;
        this.comment = comment;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
