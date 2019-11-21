package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.models.TeckerByMentor;
import org.desarrolladorslp.technovation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MentorRepository extends JpaRepository <TeckerByMentor, UUID> {

    @Modifying
    @Query(value = "INSERT INTO tecker_by_mentor (tecker_id, mentor_id) VALUES (:teckerId, :mentorId)", nativeQuery = true)
    void assignTeckerToMentor(UUID mentorId, UUID teckerId);

    @Modifying
    @Query(value = "DELETE FROM tecker_by_mentor WHERE tecker_id = :teckerId AND mentor_id = :mentorId", nativeQuery = true)
    void unassignTeckerFromMentor(UUID mentorId, UUID teckerId);

    @Query("SELECT u FROM User u JOIN TeckerByMentor tm ON u.id = tm.teckerId WHERE tm.mentorId = :mentorId")
    List<User> getTeckersByMentor(UUID mentorId);


}
