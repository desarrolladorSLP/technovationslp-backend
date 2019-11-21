package org.desarrolladorslp.technovation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.desarrolladorslp.technovation.enumerable.StatusType;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverableResourcesDTO {

    private UUID id;

    private String dueDate;

    private String title;

    private String description;

    private List<ResourceDTO> resources;

    private StatusType status;

    public DeliverableResourcesDTO(UUID id, ZonedDateTime dueDate, String title, String description, StatusType status) {
        this.id = id;
        this.dueDate = dueDate.toString();
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
