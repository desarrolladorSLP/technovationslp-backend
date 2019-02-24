package org.desarrolladorslp.technovation.config.auth;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class TechnovationSlpAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;

    public TechnovationSlpAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    TechnovationSlpAuthenticationToken(Authentication baseObject,
                                       Collection<? extends GrantedAuthority> authorities, boolean authenticated) {
        super(authorities);
        this.principal = baseObject.getPrincipal();
        this.credentials = baseObject.getCredentials();
        super.setAuthenticated(authenticated);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }
}
