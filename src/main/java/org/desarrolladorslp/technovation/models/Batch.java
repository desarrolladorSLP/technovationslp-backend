package org.desarrolladorslp.technovation.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "batches")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Batch implements Serializable {

    private static final long serialVersionUID = 6087371650604727128L;

    @Id
    private UUID id;

    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private String notes;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "program_id")
    private Program program;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Batch batch = (Batch) o;
        return id.equals(batch.id) &&
                name.equals(batch.name) &&
                Objects.equals(startDate, batch.startDate) &&
                Objects.equals(endDate, batch.endDate) &&
                Objects.equals(notes, batch.notes) &&
                program.getId().equals(batch.program.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startDate, endDate, notes, program.getId());
    }
}
