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
@Table(name = "deliverables_resources")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverableResources implements Serializable {

    private static final long serialVersionUID = 821231425368211389L;

    @Id
    @Column(name = "deliverable_id", nullable = false)
    private UUID deliverableId;

    @Id
    @Column(name = "resource_id", nullable = false)
    private UUID resourceId;
}
