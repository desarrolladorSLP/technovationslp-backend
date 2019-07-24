package org.desarrolladorslp.technovation.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "roles")
@Builder
@Data
public class Role implements Serializable {

    private static final long serialVersionUID = 7392533414353607172L;

    @Id
    @Column(updatable = false)
    private String name;

    @Column(updatable = false)
    private String description;
}
