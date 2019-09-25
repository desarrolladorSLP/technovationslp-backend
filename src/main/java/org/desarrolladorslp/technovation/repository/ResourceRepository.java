package org.desarrolladorslp.technovation.repository;

import org.desarrolladorslp.technovation.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ResourceRepository extends JpaRepository<Resource, UUID> {

    @Query("SELECT r FROM Resource r WHERE r.message.id = :messageId")
    List<Resource> getResourcesBySpecificMessage(UUID messageId);
}
