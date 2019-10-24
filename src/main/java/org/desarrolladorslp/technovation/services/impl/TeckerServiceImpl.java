package org.desarrolladorslp.technovation.services.impl;

import org.desarrolladorslp.technovation.dto.DeliverableByTeckerDTO;
import org.desarrolladorslp.technovation.models.DeliverableByTecker;
import org.desarrolladorslp.technovation.repository.TeckerRepository;
import org.desarrolladorslp.technovation.services.TeckerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeckerServiceImpl implements TeckerService {

    private TeckerRepository teckerRepository;
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public List<DeliverableByTeckerDTO> getDeliverableByTecker(UUID teckerId) {
        return teckerRepository.deliverableByTecker(teckerId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<DeliverableByTeckerDTO> getDeliverableByKid(UUID teckerId, UUID parentId) {
        Optional<Boolean> areFamily = teckerRepository.areFamily(teckerId, parentId);

        if (areFamily.isPresent())
            return teckerRepository.deliverableByTecker(teckerId).stream().map(this::convertToDTO).collect(Collectors.toList());

        throw new NoSuchElementException();
    }

    private DeliverableByTeckerDTO convertToDTO(DeliverableByTecker deliverableByTecker) {
        return modelMapper.map(deliverableByTecker, DeliverableByTeckerDTO.class);
    }

    @Autowired
    public void setTeckerRepository(TeckerRepository teckerRepository) {
        this.teckerRepository = teckerRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
