package org.desarrolladorslp.technovation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterToBatchDTO {

    private UUID batchId;

    private List<UUID> register;

    private List<UUID> unregister;
}
