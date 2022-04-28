package com.example.interestplanet.service;

import android.util.Log;
import androidx.annotation.NonNull;
import com.example.interestplanet.model.AbstractModel;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractService<T extends AbstractModel> {

    public static final FirebaseDatabase DB = FirebaseDatabase.getInstance();
    public abstract DatabaseReference getDBRef();
    public abstract Class<T> getEntityClass();

    /**
     * add or update a record
     * @param model
     */
    public void addOrUpdate(T model) {
        String id = model.getId();
        if (id == null || id.equals("")) {
            id = getDBRef().push().getKey();
            model.setId(id);
        }
        try {
            getDBRef().child(id).setValue(model);
        } catch (Exception e) {
            Log.e("Firebase", "addOrUpdate: ", e);
        }
    }

    /**
     * delete a record
     * @param id
     */
    public void delete(String id) {
        try {
            getDBRef().child(id).removeValue();
        } catch (Exception e) {
            Log.e("Firebase", "delete: ", e);
        }
    }

    public void drop() {
        try {
            getDBRef().removeValue();
        } catch (Exception e) {
            Log.e("Firebase", "drop: ", e);
        }
    }

    /**
     * query a record
     * @param id
     */
    public void find(String id, Consumer<T> action) {
        getDBRef().child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                action.accept(snapshot.getValue(getEntityClass()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "find: ", error.toException());
            }
        });
    }

    /**
     * query all records
     * @param action
     */
    public void findAll(Consumer<List<T>> action) {
        getDBRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<T> data = new ArrayList<>();
                for (DataSnapshot d : snapshot.getChildren()) {
                    T t = d.getValue(getEntityClass());
                    data.add(t);
                }
                action.accept(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "findAll: ", error.toException());
            }
        });
    }

    /**
     * query multiple records by ids
     * @param ids
     * @param action
     */
    public void findMulti(List<String> ids, Consumer<List<T>> action) {
        getDBRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<T> data = new ArrayList<>();
                for (DataSnapshot d : snapshot.getChildren()) {
                    T t = d.getValue(getEntityClass());
                    if (ids.contains(t.getId())) {
                        data.add(t);
                    }
                }
                action.accept(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "findMulti: ", error.toException());
            }
        });
    }

    public void findByField(String field, String value, Consumer<List<T>> action) {
        getDBRef().orderByChild(field)
                .equalTo(value)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<T> data = new ArrayList<>();
                        for (DataSnapshot d : snapshot.getChildren()) {
                            data.add(d.getValue(getEntityClass()));
                        }
                        action.accept(data);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "findByField: ", error.toException());
                    }
                });
    }

    public void findLikeField(String field, String value, Consumer<List<T>> action) {
        getDBRef().orderByChild(field)
                .startAt(value)
                .endAt(value + "\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<T> data = new ArrayList<>();
                        snapshot.getChildren().forEach(d -> {
                            data.add(d.getValue(getEntityClass()));
                        });
                        action.accept(data);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "findLikeField: ", error.toException());
                    }
                });
    }
}
