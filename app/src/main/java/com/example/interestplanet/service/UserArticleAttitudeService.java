package com.example.interestplanet.service;

import com.example.interestplanet.model.UserArticleAttitude;
import com.google.firebase.database.DatabaseReference;

public class UserArticleAttitudeService extends AbstractService<UserArticleAttitude> {

    @Override
    public DatabaseReference getDBRef() {
        return DB.getReference("user_article_attitudes");
    }

    @Override
    public Class<UserArticleAttitude> getEntityClass() {
        return UserArticleAttitude.class;
    }


}
