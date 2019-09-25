package org.desarrolladorslp.technovation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {

    @Id
    private String url;

    private String mimeType;
}
