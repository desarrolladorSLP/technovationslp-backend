package org.desarrolladorslp.technovation.services.impl;

import org.desarrolladorslp.technovation.dto.AssignTeckersDTO;
import org.desarrolladorslp.technovation.exception.UserDoesNotHaveRequieredRole;
import org.desarrolladorslp.technovation.repository.MentorRepository;
import org.desarrolladorslp.technovation.repository.UserRepository;
import org.desarrolladorslp.technovation.services.MentorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MentorServiceImpl implements MentorService {

    private MentorRepository mentorRepository;

    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(MentorServiceImpl.class);

    private static final String ROLE_TECKER = "ROLE_TECKER";

    @Override
    @Transactional
    public void assignTeckersToMentor(UUID mentorId, AssignTeckersDTO teckersToAssign) {

        userRepository.doesUserHaveRoleMentor(mentorId).ifPresentOrElse(
                uuid -> {
                    assignToMentor(mentorId, teckersToAssign.getAssign());
                    unassignFromMentor(mentorId, teckersToAssign.getUnassign());
                }, () -> {
                    logger.warn("User is not a mentor " + mentorId);
                    throw new UserDoesNotHaveRequieredRole("User: " + mentorId + " is not a MENTOR");
                }
        );

    }

    @Override
    @Transactional
    public void assignToMentor(UUID mentorId, List<UUID> teckersToAssing) {

        List<UUID> errorToAssign = new ArrayList<>();
        teckersToAssing.forEach(
                teckerId -> userRepository.findById(teckerId).ifPresentOrElse(
                        user -> userRepository.doesUserHaveRoleTecker(user.getId(), ROLE_TECKER).ifPresentOrElse(
                                userId -> mentorRepository.assignTeckerToMentor(mentorId, userId),
                                () -> errorToAssign.add(teckerId)
                        ), () -> errorToAssign.add(teckerId)
                ));
        if (!errorToAssign.isEmpty()) {
            logger.warn("Error to assign the users {}", errorToAssign);
        }
    }

    @Override
    @Transactional
    public void unassignFromMentor(UUID mentorId, List<UUID> teckersToUnassign) {

        List<UUID> errorToUnassign = new ArrayList<>();
        teckersToUnassign.forEach(
                uuid -> userRepository.findById(uuid).ifPresentOrElse(
                        user -> mentorRepository.unassignTeckerFromMentor(mentorId, user.getId()
                        ), () -> errorToUnassign.add(uuid)
                )
        );
        if(!errorToUnassign.isEmpty())
            logger.warn("error to unregister the users {}", errorToUnassign);
    }

    @Autowired
    public void setMentorRepository(MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
