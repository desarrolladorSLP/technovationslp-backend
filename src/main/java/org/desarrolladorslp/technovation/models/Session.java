package org.desarrolladorslp.technovation.models;

import java.time.LocalDate;
import java.time.LocalTime;
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
@Table(name = "sessions")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    private UUID id;

    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    private String title;

    private String notes;

    private String location;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "batch_id")
    private Batch batch;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(id,session.id) &&
                Objects.equals(date, session.date) &&
                Objects.equals(startTime, session.startTime) &&
                Objects.equals(endTime, session.endTime) &&
                Objects.equals(title, session.title) &&
                Objects.equals(notes, session.notes) &&
                Objects.equals(location, session.location) &&
                batch.getId().equals(session.batch.getId());

    }

    @Override
    public int hashCode() { return Objects.hash(id, date, startTime, endTime, title, notes, location, batch.getId());
    }

}
