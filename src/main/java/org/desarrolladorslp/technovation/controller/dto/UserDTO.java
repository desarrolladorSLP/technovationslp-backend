package org.desarrolladorslp.technovation.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
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

    private String roles;

}
