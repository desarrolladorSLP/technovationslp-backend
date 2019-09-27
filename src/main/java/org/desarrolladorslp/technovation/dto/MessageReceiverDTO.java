package org.desarrolladorslp.technovation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageReceiverDTO {

    @Id
    private UUID id;

    private String name;
}
