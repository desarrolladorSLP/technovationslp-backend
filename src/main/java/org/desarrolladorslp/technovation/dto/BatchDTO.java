package org.desarrolladorslp.technovation.dto;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchDTO {

    @Id
    private UUID id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private String notes;

    private UUID programId;
    
}
