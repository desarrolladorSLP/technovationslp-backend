package org.desarrolladorslp.technovation.models;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "programs")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Program {

    @Id
    private UUID id;

    private String name;
    private String description;
    private String responsible;
}
