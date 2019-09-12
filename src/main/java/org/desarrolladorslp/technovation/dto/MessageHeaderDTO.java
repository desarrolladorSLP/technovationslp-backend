package org.desarrolladorslp.technovation.dto;

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
public class MessageHeaderDTO {

    @Id
    private String sender;

    private String senderImage;

    private String subject;

    private String timestamp;
}
