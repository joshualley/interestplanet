package com.example.interestplanet.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserArticleAttitude extends AbstractModel {
    private String userArticleUnionId;
    private boolean isGood;
    private boolean isCollected;

    public UserArticleAttitude() {}

    public UserArticleAttitude(String userArticleUnionId, boolean isGood, boolean isCollected) {
        this.userArticleUnionId = userArticleUnionId;
        this.isGood = isGood;
        this.isCollected = isCollected;
    }

    public String getUserArticleUnionId() {
        return userArticleUnionId;
    }

    public void setUserArticleUnionId(String userArticleUnionId) {
        this.userArticleUnionId = userArticleUnionId;
    }

    public boolean isGood() {
        return isGood;
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }
}
