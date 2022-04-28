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
import com.example.interestplanet.adapter.PlanetListAdapter;
import com.example.interestplanet.model.Article;
import com.example.interestplanet.model.Planet;
import com.example.interestplanet.model.User;
import com.example.interestplanet.service.ServiceRegister;
import com.example.interestplanet.service.StatusStoreService;
import com.google.gson.Gson;

import java.util.List;

public class MyPlanetsActivity extends AppCompatActivity {

    private List<Planet> mPlanets;
    private ListView planetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_planets);

        // set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        planetList = findViewById(R.id.planet_list);
        planetList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            if (mPlanets == null) return;
            Planet p = mPlanets.get(position);
            Intent intent = new Intent(this, PlanetEditActivity.class);
            intent.putExtra("planet", new Gson().toJson(p));
            startActivity(intent);
        });

        User user = (User) StatusStoreService.getInstance().get("user");
        ServiceRegister.PlanetServiceInstance
                .findByField("creatorId", user.getId())
                .observe(this, planets -> {
                    mPlanets = planets;
                    PlanetListAdapter adapter = new PlanetListAdapter(this,this, mPlanets);
                    planetList.setAdapter(adapter);
                });
    }
}