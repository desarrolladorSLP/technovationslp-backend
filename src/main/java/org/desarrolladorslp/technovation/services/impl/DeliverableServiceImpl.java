package org.desarrolladorslp.technovation.services.impl;

import org.desarrolladorslp.technovation.dto.DeliverableDTO;
import org.desarrolladorslp.technovation.enumerable.StatusType;
import org.desarrolladorslp.technovation.models.Batch;
import org.desarrolladorslp.technovation.models.Deliverable;
import org.desarrolladorslp.technovation.models.TeckerAssignment;
import org.desarrolladorslp.technovation.repository.BatchRepository;
import org.desarrolladorslp.technovation.repository.DeliverableRepository;
import org.desarrolladorslp.technovation.repository.TeckerAssignmentRepository;
import org.desarrolladorslp.technovation.repository.UserRepository;
import org.desarrolladorslp.technovation.services.DeliverableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeliverableServiceImpl implements DeliverableService {

    private static final Logger logger = LoggerFactory.getLogger(DeliverableServiceImpl.class);

    private static final String ROLE_TECKER = "ROLE_TECKER";

    private DeliverableRepository deliverableRepository;

    private UserRepository userRepository;

    private BatchRepository batchRepository;

    private TeckerAssignmentRepository teckerAssignmentRepository;


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
    public void assignToDeliverable(UUID deliverableId, List<UUID> teckersToAssign) {
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
        teckerAssignmentRepository.removeAssignment(deliverableId);
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
                                                        assignToDeliverable(deliverableId, teckerId);
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
    public void assignToDeliverable(UUID deliverableId, UUID tekerId) {
        TeckerAssignment teckerAssigned = new TeckerAssignment();

        teckerAssigned.setId(UUID.randomUUID());
        teckerAssigned.setDeliverableId(deliverableId);
        teckerAssigned.setTeckerId(tekerId);
        teckerAssigned.setStatus(StatusType.TO_DO);

        teckerAssignmentRepository.save(teckerAssigned);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setTeckerToDeliverableRepository(TeckerAssignmentRepository teckerAssignmentRepository) {
        this.teckerAssignmentRepository = teckerAssignmentRepository;
    }

    @Autowired
    public void setBatchRepository(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliverableDTO> findByBatch(UUID batchId) {
        Batch batch = new Batch();

        batch.setId(batchId);
        List<Deliverable> deliverables = deliverableRepository.findByBatch(batch);

        return deliverables.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DeliverableDTO update(DeliverableDTO deliverableDTO, UUID deliverableId) {
        deliverableDTO.setId(deliverableId);
        Deliverable deliverable = convertToEntity(deliverableDTO);
        return convertToDTO(deliverableRepository.save(deliverable));
    }

    @Override
    @Transactional
    public void delete(UUID deliverableId) {

        try {
            Optional<Deliverable> deliverableOptional = deliverableRepository.findById(deliverableId);
            deliverableOptional.ifPresent(
                    deliverable -> deliverableRepository.delete(deliverable)
            );
        } catch (Exception exception) {

            logger.warn("Error trying to delete a deliverable with id {}", deliverableId, exception);
        }
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
