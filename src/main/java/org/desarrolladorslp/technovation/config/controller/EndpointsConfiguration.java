package org.desarrolladorslp.technovation.config.controller;

import java.time.LocalDate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Configuration
public class EndpointsConfiguration implements WebMvcConfigurer {

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(DefaultOAuth2AccessToken.class, new AccessTokenAdapter())
                .create();

        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();


        gsonHttpMessageConverter.setGson(gson);

        return gsonHttpMessageConverter;
    }

}
