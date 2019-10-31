package org.desarrolladorslp.technovation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.desarrolladorslp.technovation.enumerable.StatusType;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverableByTeckerDTO {

    private UUID id;

    private String title;

    private String dueDate;

    private StatusType status;
}
