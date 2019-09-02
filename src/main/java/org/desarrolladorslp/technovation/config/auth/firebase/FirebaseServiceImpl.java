package org.desarrolladorslp.technovation.config.auth.firebase;

import org.desarrolladorslp.technovation.config.auth.FirebaseService;
import org.desarrolladorslp.technovation.config.auth.TokenInfo;
import org.desarrolladorslp.technovation.exception.FirebaseTokenInvalidException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

@Service
@Profile("!fake-token-granter")
public class FirebaseServiceImpl implements FirebaseService {

    @Override
    public TokenInfo parseToken(String firebaseToken) {

        if (StringUtils.isEmpty(firebaseToken)) {
            throw new IllegalArgumentException("FirebaseTokenBlank");
        }

        try {
            FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);

            return TokenInfo.builder()
                    .uid(token.getUid())
                    .name(token.getName())
                    .email(token.getEmail())
                    .build();

        } catch (Exception e) {
            throw new FirebaseTokenInvalidException(e.getMessage());
        }
    }
}
