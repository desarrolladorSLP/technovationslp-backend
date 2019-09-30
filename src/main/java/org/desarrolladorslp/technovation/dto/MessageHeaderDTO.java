package org.desarrolladorslp.technovation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

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
