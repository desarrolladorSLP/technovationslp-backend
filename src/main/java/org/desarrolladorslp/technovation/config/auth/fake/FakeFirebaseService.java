package org.desarrolladorslp.technovation.config.auth.fake;

import java.util.Map;

import org.desarrolladorslp.technovation.config.auth.FirebaseService;
import org.desarrolladorslp.technovation.config.auth.TokenInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Service;

@Service
@Profile("fake-token-granter")
public class FakeFirebaseService implements FirebaseService {

    private FakeTokenProperties fakeTokenProperties;

    public FakeFirebaseService(FakeTokenProperties fakeTokenProperties) {
        this.fakeTokenProperties = fakeTokenProperties;
    }

    @Override
    public TokenInfo parseToken(String idToken) {
        Map<String, FakeToken> tokens = fakeTokenProperties.getFakeTokens();

        if (!tokens.containsKey(idToken)) {
            throw new InvalidTokenException("Invalid token. See application-fake-token-granter.yml");
        }

        FakeToken fakeToken = tokens.get(idToken);

        return TokenInfo.builder()
                .email(fakeToken.getEmail())
                .name(fakeToken.getName())
                .uid(idToken)
                .build();

    }
}
