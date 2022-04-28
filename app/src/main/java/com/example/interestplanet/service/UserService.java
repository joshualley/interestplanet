package com.example.interestplanet.service;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.example.interestplanet.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.function.Consumer;

public class UserService extends AbstractService<User> {
    @Override
    public DatabaseReference getDBRef() {
        return DB.getReference("users");
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    public MutableLiveData<User> findUserByName(String name) {
        MutableLiveData<User> data = new MutableLiveData<>();
        findByUsername(name, data::setValue);
        return data;
    }

    public String encryptPwd(String pwd) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pwd.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void findByUsername(String name, Consumer<User> action) {
        getDBRef().orderByChild("username")
                .equalTo(name)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User u = null;
                        for (DataSnapshot d : snapshot.getChildren()) {
                            u = d.getValue(User.class);
                            break;
                        }
                        action.accept(u);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }
}
