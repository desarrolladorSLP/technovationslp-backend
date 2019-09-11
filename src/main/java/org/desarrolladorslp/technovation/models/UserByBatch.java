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
@Table(name = "users_by_batch")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserByBatch implements Serializable {

    @Id
    @Column(name="batch_id", nullable = false)
    private UUID batchId;

    @Id
    @Column(name="user_id", nullable = false)
    private UUID userId;
}
