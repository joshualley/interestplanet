package com.example.interestplanet.service;

import java.util.HashMap;
import java.util.Map;

public class StatusStoreService {
    private Map<String, Object> data = new HashMap<>();
    private StatusStoreService() {}
    private static class SingletonInner {
        private static StatusStoreService instance = new StatusStoreService();
    }
    public static StatusStoreService getInstance() {
        return SingletonInner.instance;
    }

    public void push(String key, Object value) {
        data.put(key, value);
    }

    public Object pop(String key) {
        return data.remove(key);
    }

    public Object get(String key) {
        return data.get(key);
    }
    public void set(String key, Object value) {
        data.replace(key, value);
    }
}
