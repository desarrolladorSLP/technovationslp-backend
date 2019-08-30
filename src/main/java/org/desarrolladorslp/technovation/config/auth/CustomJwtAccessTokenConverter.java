package org.desarrolladorslp.technovation.config.auth;

import java.util.Map;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.UserService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

    private UserService userService;

    public CustomJwtAccessTokenConverter(UserService userService) {
        this.userService = userService;
    }

    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication authentication = getAccessTokenConverter().extractAuthentication(map);

        TokenInfo tokenInfo = new TokenInfo();

        tokenInfo.setUserId((String) map.get(AdditionalJWTInformation.USER_ID_KEY));
        tokenInfo.setEnabled((Boolean) map.get(AdditionalJWTInformation.ENABLED_KEY));
        tokenInfo.setValidated((Boolean) map.get(AdditionalJWTInformation.VALIDATED_KEY));
        tokenInfo.setUid((String) map.get(AdditionalJWTInformation.USER_NAME_KEY));
        tokenInfo.setClientId((String) map.get(AdditionalJWTInformation.CLIENT_ID_KEY));

        if(tokenInfo.isEnabled() && tokenInfo.isValidated()){
            User user = userService.findById(UUID.fromString(tokenInfo.getUserId()));

            tokenInfo.setEnabled(user.isEnabled());
            tokenInfo.setValidated(user.isValidated());
            tokenInfo.setEmail(user.getPreferredEmail());
            tokenInfo.setName(user.getName());
        }

        authentication.setDetails(tokenInfo);

        return authentication;
    }
}
