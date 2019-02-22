package com.iuresti.learning.backendclients.config.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import com.iuresti.learning.backendclients.services.FirebaseService;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private FirebaseAuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    private FirebaseService firebaseService;

    private AdditionalJWTInformation additionalJWTInformation;

    private JwtConfig jwtConfig;

    private ClientConfig clientConfig;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient(clientConfig.getClientId())
                .secret(passwordEncoder.encode(clientConfig.getSecret()))
                .scopes(clientConfig.getScopes())
                .authorizedGrantTypes("firebase", "refresh_token")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(3600);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();

        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(additionalJWTInformation, accessTokenConverter()));

        FirebaseTokenGranter firebaseTokenGranter = new FirebaseTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), firebaseService, authenticationManager);

        endpoints.accessTokenConverter(accessTokenConverter())
                .tokenGranter(firebaseTokenGranter)
                .tokenEnhancer(tokenEnhancerChain);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {

        if (StringUtils.isEmpty(jwtConfig.getPrivateKey()) || StringUtils.isEmpty(jwtConfig.getPublicKey())) {
            return null;
        }

        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        byte[] privateKey = Base64Utils.decodeFromString(jwtConfig.getPrivateKey());
        byte[] publicKey = Base64Utils.decodeFromString(jwtConfig.getPublicKey());

        accessTokenConverter.setSigningKey(new String(privateKey));
        accessTokenConverter.setVerifierKey(new String(publicKey));

        return accessTokenConverter;
    }

    @Autowired
    public void setAuthenticationManager(FirebaseAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setFirebaseService(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @Autowired
    public void setAdditionalJWTInformation(AdditionalJWTInformation additionalJWTInformation) {
        this.additionalJWTInformation = additionalJWTInformation;
    }

    @Autowired
    public void setJwtConfig(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Autowired
    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }
}
