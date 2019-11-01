package org.desarrolladorslp.technovation.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {

    private UUID id;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String title;

    private String notes;

    private String location;

    private UUID batchId;
}
