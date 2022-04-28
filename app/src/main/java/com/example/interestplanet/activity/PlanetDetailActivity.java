package com.example.interestplanet.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.interestplanet.R;
import com.example.interestplanet.adapter.ArticleListAdapter;
import com.example.interestplanet.model.Article;
import com.example.interestplanet.model.Planet;
import com.example.interestplanet.service.ServiceRegister;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlanetDetailActivity extends AppCompatActivity {

    private ProgressDialog progress;
    private ListView articleList;
    private EditText searchContent;
    private ImageButton searchBtn;
    private FloatingActionButton fabBtn;

    // data
    private List<Article> mArticles;

    private Planet planet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_detial);

        // get data
        Intent intent = getIntent();
        planet = new Gson().fromJson(intent.getStringExtra("planet"), Planet.class);

        // set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(planet.getName());

        // initialize the controls
        articleList = findViewById(R.id.article_list);
        searchContent = findViewById(R.id.searchContent);
        searchBtn = findViewById(R.id.searchBtn);
        fabBtn = findViewById(R.id.fab_btn);

        setOnClick();
        search();
    }

    private void setOnClick() {
        // set the click event of searching
        searchBtn.setOnClickListener(v -> search());
        // set the click event of the article list's item
        articleList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            if (mArticles == null) return;
            String articleJson = new Gson().toJson(mArticles.get(position));
            Intent intent = new Intent(this, ArticleDetailActivity.class);
            intent.putExtra("article", articleJson);
            startActivity(intent);
        });
        fabBtn.setOnClickListener(v -> {
            // start the ArticleEditActivity
            Intent intent = new Intent(this, ArticleEditActivity.class);
            intent.putExtra("planetId", planet.getId());
            startActivity(intent);
        });
    }

    private void search() {
        // show the progress dialog
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setMax(100);
        progress.setProgress(0);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.show();
        progress.setProgress(10);
        String content = searchContent.getText().toString();
        ServiceRegister.ArticleServiceInstance
                .findLikeField("title", content)
                .observe(this, articles -> {
                    // filter the articles according to the planet id
                    mArticles = articles
                            .stream()
                            .filter(article -> article.getPlanet().getId().equals(planet.getId()))
                            .collect(Collectors.toList());
                    progress.setProgress(60);
                    ArticleListAdapter adapter = new ArticleListAdapter(this, mArticles);
                    articleList.setAdapter(adapter);
                    progress.setProgress(100);
                    progress.dismiss();
                });
    }
}