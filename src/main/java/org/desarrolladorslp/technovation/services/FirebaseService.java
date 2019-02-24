package org.desarrolladorslp.technovation.services;


import org.desarrolladorslp.technovation.config.auth.TokenInfo;

public interface FirebaseService {

    TokenInfo parseToken(String idToken);

}
