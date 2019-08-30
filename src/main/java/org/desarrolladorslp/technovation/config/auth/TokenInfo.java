package org.desarrolladorslp.technovation.config.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenInfo {
    private String clientId;
    private String email;
    private String name;
    private String uid;
    private String userId;
    private boolean enabled;
    private boolean validated;
}
