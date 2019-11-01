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
public class EventDTO {

    private UUID id;

    private String type;

    private LocalDate date;

    private String subject;

    private String location;

    private LocalTime startTime;

    private LocalTime endTime;

    private String directions;

}
