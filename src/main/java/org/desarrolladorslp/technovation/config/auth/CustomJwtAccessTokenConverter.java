package org.desarrolladorslp.technovation.config.auth;

import java.util.Map;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication authentication = getAccessTokenConverter().extractAuthentication(map);

        TokenInfo tokenInfo = new TokenInfo();

        tokenInfo.setEmail((String) map.get(AdditionalJWTInformation.EMAIL_KEY));
        tokenInfo.setName((String) map.get(AdditionalJWTInformation.NAME_KEY));
        tokenInfo.setUid((String) map.get(AdditionalJWTInformation.USER_NAME_KEY));
        tokenInfo.setEnabled((Boolean) map.get(AdditionalJWTInformation.ENABLED_KEY));
        tokenInfo.setValidated((Boolean) map.get(AdditionalJWTInformation.VALIDATED_KEY));
        tokenInfo.setClientId((String) map.get(AdditionalJWTInformation.CLIENT_ID_KEY));
        tokenInfo.setUserId((String) map.get(AdditionalJWTInformation.USER_ID_KEY));

        authentication.setDetails(tokenInfo);

        return authentication;
    }
}
