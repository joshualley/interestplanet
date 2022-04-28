package com.example.interestplanet.service;

import androidx.lifecycle.MutableLiveData;
import com.example.interestplanet.model.Article;
import com.example.interestplanet.model.Planet;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class PlanetService extends AbstractService<Planet> {
    @Override
    public DatabaseReference getDBRef() {
        return DB.getReference("planets");
    }

    @Override
    public Class<Planet> getEntityClass() {
        return Planet.class;
    }

    public void findPlanet(String id, Consumer<Planet> action) {
        find(id, planet -> {
            if (planet == null) {
                action.accept(null);
                return;
            }
            ServiceRegister.UserServiceInstance.find(planet.getCreatorId(), user -> {
                planet.setCreator(user);
            });
        });
    }

    public MutableLiveData<List<Planet>> findByField(String field, String value) {
        MutableLiveData<List<Planet>> feedback = new MutableLiveData<>();
        findByField(field, value, planets -> {
            if (planets.size() == 0) {
                feedback.setValue(planets);
                return;
            }
            AtomicInteger num = new AtomicInteger();
            int total = planets.size();
            for (int i = 0; i < planets.size(); i++) {
                String uId = planets.get(i).getCreatorId();
                final int finalI = i;
                ServiceRegister.UserServiceInstance.find(uId, user -> {
                    planets.get(finalI).setCreator(user);
                    int cnt = num.incrementAndGet();
                    if (cnt == total) {
                        feedback.setValue(planets);
                    }
                });
            }
        });
        return feedback;
    }

    public MutableLiveData<List<Planet>> findLikeField(String field, String value) {
        MutableLiveData<List<Planet>> feedback = new MutableLiveData<>();
        findLikeField(field, value, planets -> {
            if (planets.size() == 0) {
                feedback.setValue(planets);
                return;
            }
            AtomicInteger num = new AtomicInteger();
            int total = planets.size();
            for (int i = 0; i < planets.size(); i++) {
                String uId = planets.get(i).getCreatorId();
                final int finalI = i;
                ServiceRegister.UserServiceInstance.find(uId, user -> {
                    planets.get(finalI).setCreator(user);
                    int cnt = num.incrementAndGet();
                    if (cnt == total) {
                        feedback.setValue(planets);
                    }
                });
            }
        });
        return feedback;
    }

    public MutableLiveData<List<Planet>> findAllPlanets() {
        MutableLiveData<List<Planet>> feedback = new MutableLiveData<>();
        findAll(planets -> {
            if (planets.size() == 0) {
                feedback.setValue(planets);
                return;
            }
            AtomicInteger num = new AtomicInteger();
            int total = planets.size();
            for (int i = 0; i < planets.size(); i++) {
                String uId = planets.get(i).getCreatorId();
                final int finalI = i;
                ServiceRegister.UserServiceInstance.find(uId, user -> {
                    planets.get(finalI).setCreator(user);
                    int cnt = num.incrementAndGet();
                    if (cnt == total) {
                        feedback.setValue(planets);
                    }
                });
            }
        });
        return feedback;
    }
}
