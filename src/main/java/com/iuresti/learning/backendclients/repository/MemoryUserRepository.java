package com.iuresti.learning.backendclients.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.iuresti.learning.backendclients.models.User;

@Repository
public class MemoryUserRepository {

    private Map<String, User> store = new HashMap<>();


    public synchronized User save(User user) {
        store.put(user.getUsername(), user);

        return user;
    }

    public synchronized User findByUsername(String userName) {
        return store.get(userName);
    }
}
