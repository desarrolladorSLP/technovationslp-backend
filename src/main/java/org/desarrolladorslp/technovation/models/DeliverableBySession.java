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
@Table(name = "deliverables_by_session")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverableBySession implements Serializable {

    private static final long serialVersionUID = 1231247459935466313L;

    @Id
    @Column(name="deliverable_id", nullable = false)
    private UUID deliverableId;

    @Id
    @Column(name="session_id", nullable = false)
    private UUID sessionId;

    @Id
    @Column(name="type", nullable = false)
    private String type;
}
