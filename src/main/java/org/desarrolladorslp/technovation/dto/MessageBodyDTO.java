package org.desarrolladorslp.technovation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageBodyDTO {

    private String body;

    private String subject;

    @Id
    private UUID messageId;

    private UUID senderId;

    private String senderName;

    private String senderImage;

    private String timestamp;

    private List<MessagesReceiversDTO> receivers;

    private List<ResourceDTO> attachments;
}
