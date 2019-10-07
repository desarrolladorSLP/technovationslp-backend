package org.desarrolladorslp.technovation.services.impl;

import org.desarrolladorslp.technovation.dto.DeliverableDTO;
import org.desarrolladorslp.technovation.models.Deliverable;
import org.desarrolladorslp.technovation.repository.DeliverableRepository;
import org.desarrolladorslp.technovation.services.DeliverableService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.UUID;

public class DeliverableServiceImpl implements DeliverableService {

    private DeliverableRepository deliverableRepository;

    private ModelMapper modelMapper;

    @Override
    @Transactional
    public DeliverableDTO save(DeliverableDTO deliverableDTO) {
        Deliverable deliverable = convertToEntity(deliverableDTO);
        deliverable.setId(UUID.randomUUID());
        return convertToDTO(deliverableRepository.save(deliverable));
    }

    @Autowired
    public void setDeliverableRepository(DeliverableRepository deliverableRepository) {
        this.deliverableRepository = deliverableRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void prepareMappings() {

        modelMapper.addMappings(new PropertyMap<Deliverable, DeliverableDTO>() {
            @Override
            public void configure() {
                map().setBatchId(source.getBatch().getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<DeliverableDTO, Deliverable>() {
            @Override
            public void configure() {
                map().getBatch().setId(source.getBatchId());
            }
        });
    }

    private Deliverable convertToEntity(DeliverableDTO deliverableDTO) {
        return modelMapper.map(deliverableDTO, Deliverable.class);
    }

    private DeliverableDTO convertToDTO(Deliverable deliverable) {
        return modelMapper.map(deliverable, DeliverableDTO.class);
    }


}
