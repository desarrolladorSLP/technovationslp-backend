package org.desarrolladorslp.technovation.config.auth;


import java.security.Principal;
import java.util.UUID;

public interface TokenInfoService {

    UUID getIdFromPrincipal(Principal principal);
}
