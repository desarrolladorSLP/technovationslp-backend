package org.desarrolladorslp.technovation.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageBodyDTO {

    private String body;

    private String subject;

    private UUID messageId;

    private UUID senderId;

    private String senderName;

    private String senderImage;

    private String timestamp;

    private List<MessageReceiverDTO> receivers;

    private List<ResourceDTO> attachments;
}
