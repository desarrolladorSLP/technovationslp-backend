package org.desarrolladorslp.technovation.config.auth.firebase;

import java.util.HashMap;
import java.util.Map;

import org.desarrolladorslp.technovation.config.auth.FirebaseService;
import org.desarrolladorslp.technovation.config.auth.TechnovationSlpAuthenticationManager;
import org.desarrolladorslp.technovation.config.auth.TechnovationSlpAuthenticationToken;
import org.desarrolladorslp.technovation.config.auth.TokenInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

public class FirebaseTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "firebase";
    private static final String FIREBASE_TOKEN_ID = "firebase-token-id";

    private FirebaseService firebaseService;

    private TechnovationSlpAuthenticationManager authenticationManager;

    public FirebaseTokenGranter(AuthorizationServerTokenServices tokenServices,
                                ClientDetailsService clientDetailsService,
                                OAuth2RequestFactory requestFactory,
                                FirebaseService firebaseService,
                                TechnovationSlpAuthenticationManager authenticationManager) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.firebaseService = firebaseService;
        this.authenticationManager = authenticationManager;
    }

    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new HashMap<>(tokenRequest.getRequestParameters());
        String firebaseTokenId = parameters.get(FIREBASE_TOKEN_ID);
        TokenInfo holder = firebaseService.parseToken(firebaseTokenId);

        String userName = holder.getUid();

        Authentication userAuth = new TechnovationSlpAuthenticationToken(userName, holder);
        ((AbstractAuthenticationToken) userAuth).setDetails(holder);

        try {
            userAuth = this.authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException ex) {
            logger.error("Authentication failed", ex);
            throw new InvalidGrantException(ex.getMessage());
        }

        if (userAuth != null && userAuth.isAuthenticated()) {
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } else {
            throw new InvalidGrantException("Could not authenticate user: " + holder.getEmail());
        }
    }

}
