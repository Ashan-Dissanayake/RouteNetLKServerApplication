package lk.ashan.routenetlkserverapllication.module.sparepart.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartRepository;
import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartStatusRepository;
import lk.ashan.routenetlkserverapllication.module.sparepart.state.SparePartState;
import lk.ashan.routenetlkserverapllication.module.sparepart.state.PartStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.sparepart.state.PartStatusFactory;
import lk.ashan.routenetlkserverapllication.module.sparepart.validation.PartCreationContext;
import lk.ashan.routenetlkserverapllication.module.sparepart.validation.PartCreationStrategy;
import lk.ashan.routenetlkserverapllication.module.sparepart.validation.PartStatusStrategy;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PartService {
    
    private final PartRepository partRepository;
    private final PartMapper partMapper;

    private final List<PartCreationStrategy> partCreationStrategies;
    private final PartStatusStrategy partStatusStrategy;
    private final PartStatusFactory partStatusFactory;
    private final PartStatusRepository partStatusRepository;
    private final PartStateTransitionHandler partStateTransitionHandler;


    @Transactional(readOnly = true)
    public List<PartDetailResponseDto> getParts(){
        return partMapper.toDtoList(partRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<PartDetailResponseDto> searchParts(@NotNull HashMap<String, String> params) {

        List<Part> parts = partRepository.findAll();

        String partCategoryId = params.get("sscategory");
        String partStatusId= params.get("sspartstatus");

        Stream<Part> partStream = parts.stream();

        if(partCategoryId!=null)partStream = partStream.filter(r->r.getPartcategory().getId() == Integer.parseInt(partCategoryId));
        if(partStatusId!=null)partStream = partStream.filter(r->r.getPartstatus().getId()==Integer.parseInt(partStatusId));

        return partMapper.toDtoList( partStream.collect(Collectors.toList()));
    }

    @Transactional
    @DisableSoftDeleteFilter
    public PartDetailResponseDto createPart(@NotNull PartCreateRequestDto dto) {

        Part part = partMapper.toEntity(dto);

        PartCreationContext context = new PartCreationContext(
                dto.getQoh(),
                dto.getRop()
        );

        partCreationStrategies.forEach(strategy -> strategy.validate(context));

        Partstatus determinedStatus = partStatusStrategy.determineStatus(context);

        SparePartState initialState = partStatusFactory.getState(determinedStatus.getName());
        initialState.validateInitial();

        part.setPartstatus(determinedStatus);

        Part saved = partRepository.save(part);

        return partMapper.toDto(saved);
    }

    @Transactional
    @DisableSoftDeleteFilter
    public PartDetailResponseDto updatePart(@NotNull PartUpdateRequestDto dto) {

        Part part = partRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Part not found"));

        // Use MapStruct to map allowed fields
        partMapper.updateFromDto(dto, part);

        // Handle status transition separately via state pattern
        if (dto.getPartstatus() != null) {
            Partstatus newStatus = partStatusRepository.findById(dto.getPartstatus().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid part status"));

            partStateTransitionHandler.transitionTo(part, newStatus);
        }

        Part saved = partRepository.save(part);
        return partMapper.toDto(saved);
    }

    @Transactional
    public List<Integer> deactivateParts(List<Integer> partIds) {
        List<Part> parts = partRepository.findAllById(partIds);

        if (parts.isEmpty()) {
            throw new ResourceNotFoundException("No parts found for the given IDs");
        }

        // Load DECOMMISSIONED status
        Partstatus decommissionedStatus = partStatusRepository.findByName("DECOMMISSIONED")
                .orElseThrow(() -> new ResourceNotFoundException("DECOMMISSIONED status not found"));

        // Transition each part using state pattern
        for (Part part : parts) {
            partStateTransitionHandler.transitionTo(part, decommissionedStatus);
            part.setDeleted(true); // mark as deleted
        }

        // Persist all parts
        partRepository.saveAll(parts);

        return parts.stream()
                .map(Part::getId)
                .collect(Collectors.toList());
    }
}
