package com.iuresti.learning.backendclients.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.iuresti.learning.backendclients.config.auth.FirebaseTokenHolder;
import com.iuresti.learning.backendclients.exception.FirebaseTokenInvalidException;
import com.iuresti.learning.backendclients.services.FirebaseService;

@Service
public class FirebaseServiceImpl implements FirebaseService {

    @Override
    public FirebaseTokenHolder parseToken(String firebaseToken) {

        if (StringUtils.isEmpty(firebaseToken)) {
            throw new IllegalArgumentException("FirebaseTokenBlank");
        }

        try {
            FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);

            return new FirebaseTokenHolder(token);
        } catch (Exception e) {
            throw new FirebaseTokenInvalidException(e.getMessage());
        }
    }
}
