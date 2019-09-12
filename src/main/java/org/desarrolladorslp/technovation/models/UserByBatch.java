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
@Table(name = "users_by_batch")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserByBatch implements Serializable {

    private static final long serialVersionUID = -2399889439289025275L;

    @Id
    @Column(name = "batch_id", nullable = false)
    private UUID batchId;

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;
}
