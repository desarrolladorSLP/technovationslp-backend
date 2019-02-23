package org.desarrolladorslp.technovation.config.auth;

import java.util.HashMap;
import java.util.Map;

import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class AdditionalJWTInformation implements TokenEnhancer {

    private IUserService userService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> info = new HashMap<>();
        User user = userService.findByUsername(oAuth2Authentication.getName());

        info.put("email", user.getEmail());
        info.put("name", user.getName());

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);

        return oAuth2AccessToken;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }
}
