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
public class TeckerDTO {

    private UUID teckerId;

    private String name;

    private String url;
}
