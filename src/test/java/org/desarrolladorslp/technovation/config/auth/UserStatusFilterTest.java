package org.desarrolladorslp.technovation.config.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.desarrolladorslp.technovation.config.controller.MainExceptionHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.util.MimeTypeUtils;

import com.google.gson.Gson;

@RunWith(MockitoJUnitRunner.class)
public class UserStatusFilterTest {

    private Gson gson = new Gson();

    @Test
    public void whenUserInactiveReturn403() throws ServletException, IOException {

        // Given:
        UserStatusFilter userStatusFilter = new UserStatusFilter();

        OAuth2AuthenticationDetails mockDetails = mock(OAuth2AuthenticationDetails.class);
        Authentication mockAuthentication = mock(OAuth2Authentication.class);
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        ServletOutputStream mockServletOutputStream = mock(ServletOutputStream.class);
        FilterChain mockFilterChain = mock(FilterChain.class);

        TokenInfo tokenInfo = new TokenInfo();
        ArgumentCaptor<byte[]> responsePayload = ArgumentCaptor.forClass(byte[].class);


        tokenInfo.setEnabled(false);
        tokenInfo.setValidated(false);

        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);
        when(mockAuthentication.getDetails()).thenReturn(mockDetails);
        when(mockDetails.getDecodedDetails()).thenReturn(tokenInfo);
        when(mockResponse.getOutputStream()).thenReturn(mockServletOutputStream);

        // When:
        userStatusFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // Then:
        verify(mockSecurityContext).getAuthentication();
        verify(mockAuthentication).getDetails();
        verify(mockResponse).setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        verify(mockResponse).setStatus(HttpStatus.FORBIDDEN.value());
        verify(mockResponse).getOutputStream();
        verify(mockServletOutputStream).write(responsePayload.capture());
        verify(mockServletOutputStream).close();
        verify(mockDetails).getDecodedDetails();
        verifyZeroInteractions(mockDetails, mockAuthentication, mockSecurityContext, mockRequest, mockResponse, mockServletOutputStream, mockFilterChain);

        MainExceptionHandler.Error error = gson.fromJson(new String(responsePayload.getValue()), MainExceptionHandler.Error.class);

        assertThat(error).extracting("message").containsExactly("User needs to be validated by administrators");
        assertThat(error).extracting("exception").containsExactly("org.desarrolladorslp.technovation.exception.InvalidUserException");
    }

    @Test
    public void whenUserNotEnabledReturn403() throws ServletException, IOException {

        // Given:
        UserStatusFilter userStatusFilter = new UserStatusFilter();

        OAuth2AuthenticationDetails mockDetails = mock(OAuth2AuthenticationDetails.class);
        Authentication mockAuthentication = mock(OAuth2Authentication.class);
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        ServletOutputStream mockServletOutputStream = mock(ServletOutputStream.class);
        FilterChain mockFilterChain = mock(FilterChain.class);

        TokenInfo tokenInfo = new TokenInfo();
        ArgumentCaptor<byte[]> responsePayload = ArgumentCaptor.forClass(byte[].class);

        tokenInfo.setEnabled(false);
        tokenInfo.setValidated(true);

        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);
        when(mockAuthentication.getDetails()).thenReturn(mockDetails);
        when(mockDetails.getDecodedDetails()).thenReturn(tokenInfo);
        when(mockResponse.getOutputStream()).thenReturn(mockServletOutputStream);

        // When:
        userStatusFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // Then:
        verify(mockSecurityContext).getAuthentication();
        verify(mockAuthentication).getDetails();
        verify(mockResponse).setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        verify(mockResponse).setStatus(HttpStatus.FORBIDDEN.value());
        verify(mockResponse).getOutputStream();
        verify(mockServletOutputStream).write(responsePayload.capture());
        verify(mockServletOutputStream).close();
        verify(mockDetails).getDecodedDetails();
        verifyZeroInteractions(mockDetails, mockAuthentication, mockSecurityContext, mockRequest, mockResponse, mockServletOutputStream, mockFilterChain);

        MainExceptionHandler.Error error = gson.fromJson(new String(responsePayload.getValue()), MainExceptionHandler.Error.class);

        assertThat(error).extracting("message").containsExactly("Inactive user");
        assertThat(error).extracting("exception").containsExactly("org.desarrolladorslp.technovation.exception.InactiveUserException");
    }

    @Test
    public void whenUserEnabledAndValidatedThenSuccess() throws ServletException, IOException {

        // Given:
        UserStatusFilter userStatusFilter = new UserStatusFilter();

        OAuth2AuthenticationDetails mockDetails = mock(OAuth2AuthenticationDetails.class);
        Authentication mockAuthentication = mock(OAuth2Authentication.class);
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        ServletOutputStream mockServletOutputStream = mock(ServletOutputStream.class);
        FilterChain mockFilterChain = mock(FilterChain.class);

        TokenInfo tokenInfo = new TokenInfo();

        tokenInfo.setEnabled(true);
        tokenInfo.setValidated(true);

        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);
        when(mockAuthentication.getDetails()).thenReturn(mockDetails);
        when(mockDetails.getDecodedDetails()).thenReturn(tokenInfo);

        // When:
        userStatusFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // Then:
        verify(mockSecurityContext).getAuthentication();
        verify(mockAuthentication).getDetails();
        verify(mockDetails).getDecodedDetails();
        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
        verifyZeroInteractions(mockDetails, mockAuthentication, mockSecurityContext, mockRequest, mockResponse, mockServletOutputStream, mockFilterChain);
    }
}
