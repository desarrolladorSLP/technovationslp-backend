package org.desarrolladorslp.technovation.services;


import org.desarrolladorslp.technovation.config.auth.FirebaseTokenHolder;

public interface FirebaseService {

    FirebaseTokenHolder parseToken(String idToken);

}
