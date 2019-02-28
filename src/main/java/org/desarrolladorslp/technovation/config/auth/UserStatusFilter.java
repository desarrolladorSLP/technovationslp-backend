package org.desarrolladorslp.technovation.config.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.desarrolladorslp.technovation.exception.InactiveUserException;
import org.desarrolladorslp.technovation.exception.InvalidUserException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserStatusFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
            TokenInfo tokenInfo = (TokenInfo) details.getDecodedDetails();

            if (!tokenInfo.isEnabled()) {
                throw new InactiveUserException("Inactive user");
            }

            if (!tokenInfo.isValidated()) {
                throw new InvalidUserException("User needs to be validated by administrators");
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
