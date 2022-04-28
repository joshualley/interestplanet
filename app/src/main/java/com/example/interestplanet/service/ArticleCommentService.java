package com.example.interestplanet.service;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.interestplanet.model.Article;
import com.example.interestplanet.model.ArticleComment;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ArticleCommentService extends AbstractService<ArticleComment> {
    @Override
    public DatabaseReference getDBRef() {
        return DB.getReference("article_comments");
    }

    @Override
    public Class<ArticleComment> getEntityClass() {
        return ArticleComment.class;
    }

    public MutableLiveData<List<ArticleComment>> findByField(String field, String value) {
        MutableLiveData<List<ArticleComment>> feedback = new MutableLiveData<>();
        findByField(field, value, comments -> {
            if (comments.size() == 0) {
                feedback.setValue(comments);
                return;
            }
            AtomicInteger num = new AtomicInteger();
            int total = comments.size() ;
            for (int i = 0; i < comments.size(); i++) {
                String uId = comments.get(i).getUserId();
                final int finalI = i;
                ServiceRegister.UserServiceInstance.find(uId, user -> {
                    comments.get(finalI).setUser(user);
                    int cnt = num.incrementAndGet();
                    // Log.i("NUM", "Total: " + total + ", Current: " + cnt);
                    if (cnt >= total) {
                        feedback.setValue(comments);
                    }
                });
            }
        });
        return feedback;
    }
}
