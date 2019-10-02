package org.desarrolladorslp.technovation.repository;

import java.util.List;
import java.util.UUID;

import org.desarrolladorslp.technovation.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResourceRepository extends JpaRepository<Resource, UUID> {

    @Query("SELECT r FROM Resource r WHERE r.messageId = :messageId")
    List<Resource> getResourcesBySpecificMessage(UUID messageId);
}
