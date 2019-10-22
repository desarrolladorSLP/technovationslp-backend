package org.desarrolladorslp.technovation.config.auth.firebase;

import org.desarrolladorslp.technovation.config.auth.FirebaseService;
import org.desarrolladorslp.technovation.config.auth.TokenInfo;
import org.desarrolladorslp.technovation.exception.FirebaseTokenInvalidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

@Service
@Profile("!fake-token-granter")
public class FirebaseServiceImpl implements FirebaseService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseServiceImpl.class);

    @Override
    public TokenInfo parseToken(String firebaseToken) {

        if (StringUtils.isEmpty(firebaseToken)) {
            throw new IllegalArgumentException("FirebaseTokenBlank");
        }

        try {

            logger.info("Identifying firebaseToken");

            FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);

            logger.info("Token valid for user: {}", token.getEmail());

            return TokenInfo.builder()
                    .uid(token.getUid())
                    .name(token.getName())
                    .email(token.getEmail())
                    .pictureUrl(token.getPicture())
                    .build();

        } catch (Exception e) {
            logger.warn("Invalid token");
            throw new FirebaseTokenInvalidException(e.getMessage());
        }
    }
}
