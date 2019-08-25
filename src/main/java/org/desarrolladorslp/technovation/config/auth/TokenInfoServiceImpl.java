package org.desarrolladorslp.technovation.config.auth;

import java.security.Principal;
import java.util.UUID;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenInfoServiceImpl implements TokenInfoService {

    @Override
    public UUID getIdFromPrincipal(Principal principal) {
        OAuth2Authentication auth = (OAuth2Authentication) principal;
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        TokenInfo tokenInfo = (TokenInfo) details.getDecodedDetails();

        return UUID.fromString(tokenInfo.getUserId());
    }
}
