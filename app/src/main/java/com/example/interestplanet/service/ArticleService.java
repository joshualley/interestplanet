package com.example.interestplanet.service;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.interestplanet.model.Article;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ArticleService extends AbstractService<Article> {
    @Override
    public DatabaseReference getDBRef() {
        return DB.getReference("articles");
    }

    @Override
    public Class<Article> getEntityClass() {
        return Article.class;
    }

    /**
     * find an article with associated fields by id
     * @param id
     * @param action
     */
    public void findArticle(String id, Consumer<Article> action) {
        find(id, article -> {
            if (article == null) {
                action.accept(null);
                return;
            }
            ServiceRegister.UserServiceInstance.find(article.getAuthorId(), user -> {
                article.setAuthor(user);
                ServiceRegister.PlanetServiceInstance.find(article.getPlanetId(), planet -> {
                    article.setPlanet(planet);
                    action.accept(article);
                });
            });
        });
    }

    public MutableLiveData<List<Article>> findByField(String field, String value) {
        MutableLiveData<List<Article>> feedback = new MutableLiveData<>();
        findByField(field, value, articles -> {
            if (articles.size() == 0) {
                feedback.setValue(articles);
                return;
            }
            AtomicInteger num = new AtomicInteger();
            int total = articles.size() * 2;
            for (int i = 0; i < articles.size(); i++) {
                String uId = articles.get(i).getAuthorId();
                String pId = articles.get(i).getPlanetId();
                final int finalI = i;
                ServiceRegister.UserServiceInstance.find(uId, user -> {
                    articles.get(finalI).setAuthor(user);
                    int cnt = num.incrementAndGet();
                    Log.i("NUM", "Total: " + total + ", Current: " + cnt);
                    if (cnt >= total) {
                        feedback.setValue(articles);
                    }
                });
                ServiceRegister.PlanetServiceInstance.find(pId, planet -> {
                    articles.get(finalI).setPlanet(planet);
                    int cnt = num.incrementAndGet();
                    Log.i("NUM", "Total: " + total + ", Current: " + cnt);
                    if (cnt >= total) {
                        feedback.setValue(articles);
                    }
                });
            }
        });
        return feedback;
    }


    public MutableLiveData<List<Article>> findLikeField(String field, String value) {
        MutableLiveData<List<Article>> feedback = new MutableLiveData<>();
        findLikeField(field, value, articles -> {
            if (articles.size() == 0) {
                feedback.setValue(articles);
                return;
            }
            AtomicInteger num = new AtomicInteger();
            int total = articles.size() * 2;
            for (int i = 0; i < articles.size(); i++) {
                String uId = articles.get(i).getAuthorId();
                String pId = articles.get(i).getPlanetId();
                final int finalI = i;
                ServiceRegister.UserServiceInstance.find(uId, user -> {
                    articles.get(finalI).setAuthor(user);
                    int cnt = num.incrementAndGet();
                    Log.i("NUM", "Total: " + total + ", Current: " + cnt);
                    if (cnt >= total) {
                        feedback.setValue(articles);
                    }
                });
                ServiceRegister.PlanetServiceInstance.find(pId, planet -> {
                    articles.get(finalI).setPlanet(planet);
                    int cnt = num.incrementAndGet();
                    Log.i("NUM", "Total: " + total + ", Current: " + cnt);
                    if (cnt >= total) {
                        feedback.setValue(articles);
                    }
                });
            }
        });
        return feedback;
    }

    public MutableLiveData<List<Article>> findAllArticles() {
        MutableLiveData<List<Article>> feedback = new MutableLiveData<>();
        findAll(articles -> {
            if (articles.size() == 0) {
                feedback.setValue(articles);
                return;
            }
            AtomicInteger num = new AtomicInteger();
            int total = articles.size() * 2;
            for (int i = 0; i < articles.size(); i++) {
                String uId = articles.get(i).getAuthorId();
                String pId = articles.get(i).getPlanetId();
                final int finalI = i;
                ServiceRegister.UserServiceInstance.find(uId, user -> {
                    articles.get(finalI).setAuthor(user);
                    int cnt = num.incrementAndGet();
                    if (cnt == total) {
                        feedback.setValue(articles);
                    }
                });
                ServiceRegister.PlanetServiceInstance.find(pId, planet -> {
                    articles.get(finalI).setPlanet(planet);
                    int cnt = num.incrementAndGet();
                    if (cnt == total) {
                        feedback.setValue(articles);
                    }
                });
            }
        });
        return feedback;
    }
}
