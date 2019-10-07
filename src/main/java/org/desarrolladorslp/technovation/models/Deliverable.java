package org.desarrolladorslp.technovation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "deliverables")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deliverable {

    @Id
    private UUID id;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "batch_id")
    private Batch batch;

}
