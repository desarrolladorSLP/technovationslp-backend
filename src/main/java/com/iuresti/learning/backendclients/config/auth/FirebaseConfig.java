package com.iuresti.learning.backendclients.config.auth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Configuration
public class FirebaseConfig {

    @Value("${authentication.firebase.databaseUrl}")
    private String databaseUrl;

    @Value("${authentication.firebase.configObject}")
    private String configObject;

    @Bean
    public DatabaseReference firebaseDatabase() {
        return StringUtils.isEmpty(configObject) ? null : FirebaseDatabase.getInstance().getReference();
    }

    @PostConstruct
    public void init() throws IOException {

        if (!StringUtils.isEmpty(configObject)) {
            InputStream serviceAccount = new ByteArrayInputStream(Base64Utils.decodeFromString(configObject));

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }
}
