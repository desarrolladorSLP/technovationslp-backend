package org.desarrolladorslp.technovation.config.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.desarrolladorslp.technovation.config.auth.AdditionalJWTInformation.CLIENT_ID_KEY;
import static org.desarrolladorslp.technovation.config.auth.AdditionalJWTInformation.EMAIL_KEY;
import static org.desarrolladorslp.technovation.config.auth.AdditionalJWTInformation.ENABLED_KEY;
import static org.desarrolladorslp.technovation.config.auth.AdditionalJWTInformation.NAME_KEY;
import static org.desarrolladorslp.technovation.config.auth.AdditionalJWTInformation.USER_ID_KEY;
import static org.desarrolladorslp.technovation.config.auth.AdditionalJWTInformation.VALIDATED_KEY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;

@RunWith(MockitoJUnitRunner.class)
public class CustomJwtAccessTokenConverterTest {

    @Test
    public void extractAuthenticationForNotEnabledAndNotValidatedUsers() {
        extractAuthenticationTestingEnabledAndValidatedFlags(false, false);
    }

    @Test
    public void extractAuthenticationForNotEnabledAndValidatedUsers() {
        extractAuthenticationTestingEnabledAndValidatedFlags(false, true);
    }

    @Test
    public void extractAuthenticationForEnabledAndNotValidatedUsers() {
        extractAuthenticationTestingEnabledAndValidatedFlags(true, false);
    }

    @Test
    public void extractAuthenticationForEnabledAndValidatedUsers() {
        extractAuthenticationForEnabledAndValidatedUsers(true, true);
    }

    @Test
    public void extractAuthenticationForNotEnabledAndValidatedUsersInDBUser() {
        extractAuthenticationForEnabledAndValidatedUsers(false, false);
    }

    @Test
    public void extractAuthenticationForEnabledAndNotValidatedUsersInDBUser() {
        extractAuthenticationForEnabledAndValidatedUsers(false, true);
    }

    @Test
    public void extractAuthenticationForNotEnabledAndNotValidatedUsersInDBUser() {
        extractAuthenticationForEnabledAndValidatedUsers(false, false);
    }

    public void extractAuthenticationForEnabledAndValidatedUsers(boolean enabled, boolean validated) {
        // Given:
        UserService mockUserService = mock(UserService.class);
        AccessTokenConverter mockAccessTokenConverter = mock(AccessTokenConverter.class);
        CustomJwtAccessTokenConverter tokenConverter = new CustomJwtAccessTokenConverter(mockUserService);
        Map<String, Object> mapWithTokenInfo = prepareMapWithAuthenticationInfo();
        OAuth2Authentication authentication = mock(OAuth2Authentication.class);
        User user = User.builder()
                .name(UUID.randomUUID().toString())
                .preferredEmail(UUID.randomUUID().toString())
                .validated(validated)
                .enabled(enabled)
                .build();

        mapWithTokenInfo.put(ENABLED_KEY, true);
        mapWithTokenInfo.put(VALIDATED_KEY, true);

        TokenInfo tokenInfo = TokenInfo.builder()
                .clientId((String) mapWithTokenInfo.get(CLIENT_ID_KEY))
                .userId((String) mapWithTokenInfo.get(USER_ID_KEY))
                .enabled(user.isEnabled())
                .validated(user.isValidated())
                .name(user.getName())
                .email(user.getPreferredEmail())
                .build();

        tokenConverter.setAccessTokenConverter(mockAccessTokenConverter);
        when(mockAccessTokenConverter.extractAuthentication(mapWithTokenInfo)).thenReturn(authentication);
        UUID userUUID = UUID.fromString(tokenInfo.getUserId());
        when(mockUserService.findById(userUUID)).thenReturn(user);

        // When:
        OAuth2Authentication receivedAuthentication = tokenConverter.extractAuthentication(mapWithTokenInfo);

        //Then:
        verify(authentication).setDetails(tokenInfo);
        verify(mockUserService).findById(userUUID);
        verifyZeroInteractions(mockUserService);
        assertThat(receivedAuthentication).isEqualTo(authentication);

    }

    private void extractAuthenticationTestingEnabledAndValidatedFlags(boolean enabled, boolean validated) {
        // Given:
        UserService mockUserService = mock(UserService.class);
        AccessTokenConverter mockAccessTokenConverter = mock(AccessTokenConverter.class);
        CustomJwtAccessTokenConverter tokenConverter = new CustomJwtAccessTokenConverter(mockUserService);
        Map<String, Object> mapWithTokenInfo = prepareMapWithAuthenticationInfo();
        OAuth2Authentication authentication = mock(OAuth2Authentication.class);

        mapWithTokenInfo.put(ENABLED_KEY, enabled);
        mapWithTokenInfo.put(VALIDATED_KEY, validated);

        TokenInfo tokenInfo = TokenInfo.builder()
                .clientId((String) mapWithTokenInfo.get(CLIENT_ID_KEY))
                .userId((String) mapWithTokenInfo.get(USER_ID_KEY))
                .enabled(enabled)
                .validated(validated)
                .build();

        tokenConverter.setAccessTokenConverter(mockAccessTokenConverter);
        when(mockAccessTokenConverter.extractAuthentication(mapWithTokenInfo)).thenReturn(authentication);

        // When:
        OAuth2Authentication receivedAuthentication = tokenConverter.extractAuthentication(mapWithTokenInfo);

        //Then:
        verify(authentication).setDetails(tokenInfo);
        verifyZeroInteractions(mockUserService);
        assertThat(receivedAuthentication).isEqualTo(authentication);

    }

    private Map<String, Object> prepareMapWithAuthenticationInfo() {
        Map<String, Object> map = new HashMap<>();

        map.put(EMAIL_KEY, UUID.randomUUID().toString());
        map.put(NAME_KEY, UUID.randomUUID().toString());
        map.put(USER_ID_KEY, UUID.randomUUID().toString());
        map.put(CLIENT_ID_KEY, UUID.randomUUID().toString());

        return map;
    }

}
