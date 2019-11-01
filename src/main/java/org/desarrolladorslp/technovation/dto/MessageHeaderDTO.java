package org.desarrolladorslp.technovation.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageHeaderDTO {

    private UUID messageId;

    private String sender;

    private String senderImage;

    private String subject;

    private String timestamp;

    private UUID userSenderId;
}
