package org.desarrolladorslp.technovation.services.impl;

import org.desarrolladorslp.technovation.config.auth.FirebaseTokenHolder;
import org.desarrolladorslp.technovation.exception.FirebaseTokenInvalidException;
import org.desarrolladorslp.technovation.services.FirebaseService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

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
