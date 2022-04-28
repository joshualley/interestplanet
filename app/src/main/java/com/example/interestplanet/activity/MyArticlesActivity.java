package com.example.interestplanet.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.interestplanet.R;
import com.example.interestplanet.adapter.ArticleListAdapter;
import com.example.interestplanet.model.Article;
import com.example.interestplanet.model.User;
import com.example.interestplanet.service.ServiceRegister;
import com.example.interestplanet.service.StatusStoreService;
import com.google.gson.Gson;

import java.util.List;

public class MyArticlesActivity extends AppCompatActivity {

    private List<Article> mArticles;
    private ListView articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_articles);

        // set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        articleList = findViewById(R.id.article_list);
        articleList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            if (mArticles == null) return;
            // enter the edit activity
            Article article = mArticles.get(position);
            String articleJson = new Gson().toJson(article);
            Intent intent = new Intent(this, ArticleEditActivity.class);
            intent.putExtra("article", articleJson);
            intent.putExtra("planetId", article.getPlanetId());
            startActivity(intent);
        });

        User user = (User) StatusStoreService.getInstance().get("user");
        ServiceRegister.ArticleServiceInstance
                .findByField("authorId", user.getId())
                .observe(this, articles -> {
                    mArticles = articles;
                    ArticleListAdapter adapter = new ArticleListAdapter(this, mArticles);
                    articleList.setAdapter(adapter);
                });
    }
}