package org.desarrolladorslp.technovation.config.auth;

import static org.desarrolladorslp.technovation.config.auth.SecurityServerConfig.FAKE_PICTURES_URL;
import static org.desarrolladorslp.technovation.config.auth.SecurityServerConfig.OAUTH_TOKEN_URL;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, OAUTH_TOKEN_URL).permitAll()
                .antMatchers(HttpMethod.GET, FAKE_PICTURES_URL).permitAll()
                .anyRequest().authenticated()
                .and().addFilterAfter(userStatusFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public Filter userStatusFilter() {
        return new UserStatusFilter();
    }
}
