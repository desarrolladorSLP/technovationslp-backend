package org.desarrolladorslp.technovation.controller.dto;

import java.util.List;
import java.util.UUID;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Id
    private UUID id;

    private String name;

    private String preferredEmail;

    private boolean enabled;

    private boolean validated;

    private List<String> roles;

}
