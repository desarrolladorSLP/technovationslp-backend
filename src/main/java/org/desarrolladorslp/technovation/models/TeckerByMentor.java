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
@Table(name = "tecker_by_mentor")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeckerByMentor implements Serializable {

    private static final long serialVersionUID = 1234659439289025274L;

    @Id
    @Column(name = "tecker_id", nullable = false)
    private UUID teckerId;

    @Column(name = "mentor_id", nullable = false)
    private UUID mentorId;


}
