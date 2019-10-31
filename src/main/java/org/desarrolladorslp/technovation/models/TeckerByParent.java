package org.desarrolladorslp.technovation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "teckers_by_parents")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeckerByParent implements Serializable {

    private static final long serialVersionUID = 2349889439289025274L;

    @Id
    @Column(name = "tecker_id", nullable = false)
    private UUID teckerId;

    @Id
    @Column(name = "parent_id", nullable = false)
    private UUID parentId;
}
