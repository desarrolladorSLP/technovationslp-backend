package org.desarrolladorslp.technovation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.desarrolladorslp.technovation.enumerable.StatusType;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverableResourcesDTO {

    private UUID id;

    private String dueDate;

    private String title;

    private String description;

    private UUID batchId;

    private List<ResourceDTO> resources;

    private StatusType status;
}
