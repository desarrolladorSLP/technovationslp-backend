package org.desarrolladorslp.technovation.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {

    @Id
    private UUID id;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String title;

    private String notes;

    private String location;

    private UUID batchId;


}
