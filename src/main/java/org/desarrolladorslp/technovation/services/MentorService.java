package org.desarrolladorslp.technovation.services;

import org.desarrolladorslp.technovation.dto.AssignTeckersDTO;
import org.desarrolladorslp.technovation.dto.TeckerDTO;

import java.util.List;
import java.util.UUID;

public interface MentorService {

    void assignTeckersToMentor(UUID mentorId, AssignTeckersDTO teckersToAssign);

    void assignToMentor(UUID mentorId, List<UUID> teckersToAssign);

    void unassignFromMentor(UUID mentorId, List<UUID> teckerToUnassing);

    List<TeckerDTO> getTeckersByMentor(UUID mentorId);
}
