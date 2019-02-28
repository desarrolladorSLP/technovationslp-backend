package org.desarrolladorslp.technovation.config.auth;

import java.util.Map;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication authentication = getAccessTokenConverter().extractAuthentication(map);

        TokenInfo tokenInfo = new TokenInfo();

        tokenInfo.setEmail((String) map.get("email"));
        tokenInfo.setName((String) map.get("name"));
        tokenInfo.setUid((String) map.get("user_name"));
        tokenInfo.setEnabled((Boolean) map.get("enabled"));
        tokenInfo.setValidated((Boolean) map.get("validated"));

        authentication.setDetails(tokenInfo);

        return authentication;
    }
}
