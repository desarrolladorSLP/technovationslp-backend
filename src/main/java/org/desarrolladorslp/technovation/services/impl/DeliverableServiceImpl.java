package org.desarrolladorslp.technovation.services.impl;

import org.desarrolladorslp.technovation.dto.DeliverableDTO;
import org.desarrolladorslp.technovation.enumerable.StatusType;
import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.models.Deliverable;
import org.desarrolladorslp.technovation.models.TeckerAssigned;
import org.desarrolladorslp.technovation.repository.BatchRepository;
import org.desarrolladorslp.technovation.repository.DeliverableRepository;
import org.desarrolladorslp.technovation.repository.TeckerAssignedRepository;
import org.desarrolladorslp.technovation.repository.UserRepository;
import org.desarrolladorslp.technovation.services.DeliverableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeliverableServiceImpl implements DeliverableService {

    private static final Logger logger = LoggerFactory.getLogger(DeliverableServiceImpl.class);

    private static final String ROLE_TECKER = "ROLE_TECKER";

    private DeliverableRepository deliverableRepository;

    private UserRepository userRepository;

    private BatchRepository batchRepository;

    private TeckerAssignedRepository teckerAssignedRepository;

    @Override
    @Transactional
    public DeliverableDTO save(DeliverableDTO deliverableDTO) {
        deliverableDTO.setId(UUID.randomUUID());
        Deliverable deliverable = convertToEntity(deliverableDTO);
        return convertToDTO(deliverableRepository.save(deliverable));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Deliverable> findById(UUID id) {
        return deliverableRepository.findById(id);
    }

    @Override
    @Transactional
    public void AssignToDeliverable(UUID deliverableId, List<UUID> teckersToAssign) {
        Optional<Deliverable> deliverableOptional = findById(deliverableId);
        deliverableOptional.ifPresentOrElse(
                deliverable -> {
                    if (teckersToAssign.isEmpty()) {
                        logger.warn("List empty ");
                        removeAssignmentFromThisDeliverableToTecker(deliverableId);
                    } else
                        assignTeckerToDeliverable(deliverable.getId(), deliverable.getBatch().getId(), teckersToAssign);
                }, () -> logger.warn("deliverable doesn't exist {}", deliverableId)
        );
    }

    @Override
    @Transactional
    public void removeAssignmentFromThisDeliverableToTecker(UUID deliverableId) {
        teckerAssignedRepository.removeAssignment(deliverableId);
    }

    @Override
    @Transactional
    public void assignTeckerToDeliverable(UUID deliverableId, UUID batchId, List<UUID> teckersToAssign) {
        List<UUID> errorToAssign = new ArrayList<>();

        teckersToAssign.forEach(
                uuid -> {
                    userRepository.findById(uuid).ifPresentOrElse(
                            user -> {
                                userRepository.doesUserHaveRoleTecker(user.getId(), ROLE_TECKER).ifPresentOrElse(
                                        teckerId -> {
                                            batchRepository.areTeckerAndDeliverableAssignedInTheSameBatch(teckerId, batchId).ifPresentOrElse(
                                                    sameBatch -> {
                                                        logger.warn("deliverable and tecker in the same batch");
                                                        AssignToDeliverable(deliverableId, teckerId);
                                                    }, () -> {
                                                        logger.warn("tecker and deliverable aren't in the same batch");
                                                        errorToAssign.add(teckerId);
                                                    });
                                        }, () -> {
                                            logger.warn("user don't have tecker role {}", user.getId());
                                            errorToAssign.add(user.getId());
                                        });
                            }, () -> {
                                logger.warn("user doesn't exist {}", uuid);
                                errorToAssign.add(uuid);
                            });
                });

        if (!errorToAssign.isEmpty())
            logger.warn("error to register the users {}", errorToAssign);
    }

    @Override
    @Transactional
    public void AssignToDeliverable(UUID deliverableId, UUID tekerId) {
        TeckerAssigned teckerAssigned = new TeckerAssigned();

        teckerAssigned.setId(UUID.randomUUID());
        teckerAssigned.setDeliverableId(deliverableId);
        teckerAssigned.setTeckerId(tekerId);
        teckerAssigned.setStatus(StatusType.TO_DO);

        teckerAssignedRepository.save(teckerAssigned);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setTeckerToDeliverableRepository(TeckerAssignedRepository teckerAssignedRepositoryRepository) {
        this.teckerAssignedRepository = teckerAssignedRepositoryRepository;
    }

    @Autowired
    public void setBatchRepository(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Autowired
    public void setDeliverableRepository(DeliverableRepository deliverableRepository) {
        this.deliverableRepository = deliverableRepository;
    }

    private Deliverable convertToEntity(DeliverableDTO deliverableDTO) {
        return Deliverable.builder()
                .id(deliverableDTO.getId())
                .dueDate(ZonedDateTime.parse(deliverableDTO.getDueDate(), DateTimeFormatter.ISO_DATE_TIME))
                .title(deliverableDTO.getTitle())
                .description(deliverableDTO.getDescription())
                .batch(Batch.builder().id(deliverableDTO.getBatchId()).build())
                .build();
    }

    private DeliverableDTO convertToDTO(Deliverable deliverable) {
        return DeliverableDTO.builder()
                .id(deliverable.getId())
                .dueDate(deliverable.getDueDate().format(DateTimeFormatter.ISO_DATE_TIME))
                .title(deliverable.getTitle())
                .description(deliverable.getDescription())
                .batchId(deliverable.getBatch().getId())
                .build();
    }


}
