package org.desarrolladorslp.technovation.config.auth;

import java.util.Arrays;

import javax.sql.DataSource;

import org.desarrolladorslp.technovation.config.auth.firebase.FirebaseTokenGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private TechnovationSlpAuthenticationManager authenticationManager;

    private FirebaseService firebaseService;

    private AdditionalJWTInformation additionalJWTInformation;

    private JwtConfig jwtConfig;

    private DataSource dataSource;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource).passwordEncoder(getPasswordEncoder());
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

        JwtAccessTokenConverter accessTokenConverter = new CustomJwtAccessTokenConverter();
        byte[] privateKey = Base64Utils.decodeFromString(jwtConfig.getPrivateKey());
        byte[] publicKey = Base64Utils.decodeFromString(jwtConfig.getPublicKey());

        accessTokenConverter.setSigningKey(new String(privateKey));
        accessTokenConverter.setVerifierKey(new String(publicKey));

        return accessTokenConverter;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Autowired
    public void setAuthenticationManager(TechnovationSlpAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
