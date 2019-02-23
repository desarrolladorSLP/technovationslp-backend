package org.desarrolladorslp.technovation.repository;

import java.util.HashMap;
import java.util.Map;

import org.desarrolladorslp.technovation.models.User;
import org.springframework.stereotype.Repository;

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
