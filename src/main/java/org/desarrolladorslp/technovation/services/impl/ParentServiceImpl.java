package org.desarrolladorslp.technovation.services.impl;

import org.desarrolladorslp.technovation.dto.TeckerDTO;
import org.desarrolladorslp.technovation.exception.CannnotAssignTeckersToParent;
import org.desarrolladorslp.technovation.models.User;
import org.desarrolladorslp.technovation.repository.TeckerByParentRepository;
import org.desarrolladorslp.technovation.repository.UserRepository;
import org.desarrolladorslp.technovation.services.ParentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParentServiceImpl implements ParentService {

    private static final Logger logger = LoggerFactory.getLogger(ParentServiceImpl.class);

    private TeckerByParentRepository teckerByParentRepository;

    private UserRepository userRepository;

    private static final String ROLE_TECKER = "ROLE_TECKER";


    @Override
    @Transactional
    public void assignToParent(UUID parentId, List<UUID> teckers) {
        Optional<Boolean> optionalParent = teckerByParentRepository.isParent(parentId);

        if (optionalParent.isPresent()) {
            if (teckers.isEmpty()) {
                logger.warn("List empty");
                teckerByParentRepository.removeAssignment(parentId);
            } else
                assignTeckersToParent(parentId, teckers);
        } else {
            throw new CannnotAssignTeckersToParent("the user don't have the role PARENT");
        }
    }

    @Override
    @Transactional
    public void assignTeckersToParent(UUID parentId, List<UUID> teckers) {

        List<UUID> errorToAssign = new ArrayList<>();
        teckerByParentRepository.removeAssignment(parentId);

        teckers.forEach(
                id -> {
                    userRepository.doesUserHaveRoleTecker(id, ROLE_TECKER).ifPresentOrElse(
                            idTecker -> {
                                teckerByParentRepository.assignmentToParent(parentId, idTecker);
                            }, () -> {
                                logger.warn("user don't have tecker role {}", id);
                                errorToAssign.add(id);
                            });
                });

        if (!errorToAssign.isEmpty())
            logger.warn("error to register the users {}", errorToAssign);
    }


    @Override
    @Transactional
    public List<TeckerDTO> teckersByParent(UUID parentId) {
        Optional<Boolean> optionalParent = teckerByParentRepository.isParent(parentId);

        if (optionalParent.isPresent()) {
            List<User> teckers = teckerByParentRepository.getTeckersByParent(parentId);
            return teckers.stream().map(this::convertTeckerDTO).collect(Collectors.toList());
        }

        throw new CannnotAssignTeckersToParent("the user don't have the role PARENT");
    }

    @Override
    @Transactional
    public List<TeckerDTO> teckerByParentLogged(UUID parentId) {
        List<User> teckers = teckerByParentRepository.getTeckersByParent(parentId);
        return teckers.stream().map(this::convertTeckerDTO).collect(Collectors.toList());
    }


    @Autowired
    public void setTeckerByParentRepository(TeckerByParentRepository teckerByParentRepository) {
        this.teckerByParentRepository = teckerByParentRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private TeckerDTO convertTeckerDTO(User tecker) {
        return TeckerDTO.builder()
                .teckerId(tecker.getId())
                .name(tecker.getName())
                .pictureUrl(tecker.getPictureUrl())
                .build();
    }

}
