package org.desarrolladorslp.technovation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.desarrolladorslp.technovation.Enum.RelationType;

import javax.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(name="type", length = 100,nullable = false)
    private RelationType type;
}
