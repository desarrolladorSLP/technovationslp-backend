package org.desarrolladorslp.technovation.models;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_roles")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersByRole implements Serializable {

    private static final long serialVersionUID = 8147277458935466313L;

    @Id
    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;
}
