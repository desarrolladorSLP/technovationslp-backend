package org.desarrolladorslp.technovation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "users_roles")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersByRole implements Serializable {
    @Id
    @Column(name="role_name",nullable=false)
    private String roleName;

    @Id
    @Column(name="user_id",nullable=false)
    private UUID userId;
}
