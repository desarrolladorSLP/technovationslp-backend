package org.desarrolladorslp.technovation.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

   private String type;

   private int month;

   private int day;

   private String subject;

   private String location;

   private LocalTime startTime;

   private LocalTime endTime;

   private String directions;

}
