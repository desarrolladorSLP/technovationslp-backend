package org.desarrolladorslp.technovation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverableDTO {

    private UUID id;

    private ZonedDateTime dueDate;

    private String title;

    private String description;

    private UUID batchId;
}
