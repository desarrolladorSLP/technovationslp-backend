package org.desarrolladorslp.technovation.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchDTO {

    private UUID id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private String notes;

    private UUID programId;

}
