package org.desarrolladorslp.technovation.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageHeaderDTO {

    @Id
    private String sender;

    private String senderImage;

    private String subject;

    private String timestamp;
}
