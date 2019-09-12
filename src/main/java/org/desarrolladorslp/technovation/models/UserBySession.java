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
@Table(name = "confirm_attendance")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBySession implements Serializable {

    private static final long serialVersionUID = 7814368125368211389L;

    @Id
    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;
}
