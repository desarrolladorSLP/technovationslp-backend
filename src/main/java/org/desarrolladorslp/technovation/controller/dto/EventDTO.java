package org.desarrolladorslp.technovation.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

   private String type;

   private int month;

   private int day;

   private LocalDate date;

   private String subject;

   private String location;

   private LocalTime startTime;

   private LocalTime endTime;

   private String directions;

}
