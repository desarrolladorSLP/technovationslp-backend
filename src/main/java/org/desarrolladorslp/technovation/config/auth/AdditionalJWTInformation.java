package org.desarrolladorslp.technovation.config.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class AdditionalJWTInformation implements TokenEnhancer {

    private static final String EMAIL_KEY = "email";
    private static final String NAME_KEY = "name";
    private static final String ENABLED_KEY = "enabled";
    private static final String VALIDATED_KEY = "validated";
    private static final String ROLES = "roles";
    private UserService userService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> info = new HashMap<>();
        User user = userService.findByUsername(oAuth2Authentication.getName()).orElseThrow();

        info.put(EMAIL_KEY, user.getPreferredEmail());
        info.put(NAME_KEY, user.getName());
        info.put(ENABLED_KEY, user.isEnabled());
        info.put(VALIDATED_KEY, user.isValidated());
        info.put("userId", user.getId());
        info.put(ROLES, oAuth2Authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);

        return oAuth2AccessToken;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
