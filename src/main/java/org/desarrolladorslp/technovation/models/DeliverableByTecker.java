package org.desarrolladorslp.technovation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.desarrolladorslp.technovation.enumerable.StatusType;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliverableByTecker implements Serializable {

    private UUID id;

    private String title;

    private ZonedDateTime dueDate;

    private StatusType status;

}
