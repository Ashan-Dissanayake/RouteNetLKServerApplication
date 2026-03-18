package lk.ashan.routenetlkserverapllication.module.grn.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.mapper.GrnMapper;
import lk.ashan.routenetlkserverapllication.module.grn.mapper.GrnPartMapper;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnPart;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnRepository;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnStatusRepository;
import lk.ashan.routenetlkserverapllication.module.grn.state.GrnState;
import lk.ashan.routenetlkserverapllication.module.grn.state.GrnStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.grn.state.GrnStatusFactory;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GrnService {

    private final GrnRepository grnRepository;
    private final GrnStatusRepository grnStatusRepository;
    private final GrnMapper grnMapper;
    private final GrnPartMapper grnPartMapper;

    private final GrnStatusFactory grnStatusFactory;
    private final GrnStateTransitionHandler grnStateTransitionHandler;

    @Transactional(readOnly = true)
    public List<GrnDetailResponseDto> getGrns(){
        return grnMapper.toDtoList(grnRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<GrnDetailResponseDto> searchGrns(@NotNull HashMap<String, String> params) {

        List<Grn> grns = grnRepository.findAll();

        String number = params.get("ssnumber");
        String partRequestId = params.get("sspartrequest");
        String grnStatusId= params.get("ssgrnstatus");

        Stream<Grn> grnStream = grns.stream();

        if(number!=null)grnStream = grnStream.filter(r->r.getNumber().equals(number));
        if(partRequestId!=null)grnStream = grnStream.filter(r->r.getPartrequest().getId()==Integer.parseInt(partRequestId));
        if(grnStatusId!=null)grnStream = grnStream.filter(r->r.getGrnstatus().getId()==Integer.parseInt(grnStatusId));

        return grnMapper.toDtoList( grnStream.collect(Collectors.toList()));
    }

    @Transactional
    public GrnDetailResponseDto createGrn(@NotNull GrnCreateRequestDto createRequestDto){
        if (createRequestDto.getGrnparts() == null || createRequestDto.getGrnparts().isEmpty()) {
            throw new BusinessRuleViolationException(
                    "GRN must contain at least one part"
            );
        }

        createRequestDto.getGrnparts().forEach(item -> {
            if (item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessRuleViolationException(
                        "Received quantity must be greater than zero"
                );
            }
        });

        Grn grn = grnMapper.toEntity(createRequestDto);

        GrnStatus initialStatus = grnStatusRepository.findByName("Pending")
                .orElseThrow(() -> new IllegalStateException("Initial status PENDING not found"));

        GrnState initialState = grnStatusFactory.getState(initialStatus.getName());
        initialState.validateInitial();

        grn.setGrnstatus(initialStatus);

        Grn saved = grnRepository.save(grn);

        return grnMapper.toDto(saved);
    }

    @Transactional
    public GrnDetailResponseDto updateGrn(@NotNull GrnUpdateRequestDto dto) {

        Grn existing = grnRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "GRN not found with id " + dto.getId()
                ));

        String currentStatus = existing.getGrnstatus().getName();

        // Only PENDING GRNs are editable
        if (!"PENDING".equalsIgnoreCase(currentStatus)) {
            throw new InvalidStateTransitionException(
                    "Only PENDING GRNs can be updated"
            );
        }

        // Validate GRN items
        if (dto.getGrnparts() == null || dto.getGrnparts().isEmpty()) {
            throw new BusinessRuleViolationException(
                    "GRN must contain at least one part"
            );
        }

        dto.getGrnparts().forEach(item -> {
            if (item.getQuantity() == null || item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessRuleViolationException(
                        "Received quantity must be greater than zero"
                );
            }
        });

        // Map allowed fields from DTO → entity
       Grn mappedGrn = grnMapper.updateEntityFromDto(existing, dto);

        // Clear existing items and map new ones
        existing.getGrnparts().clear();
        dto.getGrnparts().forEach(itemDto -> {
            GrnPart part = grnPartMapper.toEntity(itemDto);
            part.setGrn(existing);
            existing.getGrnparts().add(part);
        });

        return grnMapper.toDto(mappedGrn);
    }

    @Transactional
    public GrnDetailResponseDto completeGrn(@NotNull Integer grnId) {
        Grn grn = grnRepository.findById(grnId)
                .orElseThrow(() -> new ResourceNotFoundException("GRN not found with id " + grnId));

        // Ensure only PENDING can be completed
        if (!"PENDING".equalsIgnoreCase(grn.getGrnstatus().getName())) {
            throw new InvalidStateTransitionException("Only PENDING GRNs can be completed");
        }

        // Transition state via handler
        GrnStatus completedStatus = grnStatusRepository.findByName("Completed")
                .orElseThrow(() -> new IllegalStateException("COMPLETED status not found"));

        grnStateTransitionHandler.transitionTo(grn, completedStatus);

        // Update stock for each part
        grn.getGrnparts().forEach(part -> {
            part.getPart().setQoh(part.getPart().getQoh().add(part.getQuantity()));
        });

        Grn saved = grnRepository.save(grn);

        return grnMapper.toDto(saved);
    }

    @Transactional
    public GrnDetailResponseDto cancelGrn(@NotNull Integer grnId) {
        Grn grn = grnRepository.findById(grnId)
                .orElseThrow(() -> new ResourceNotFoundException("GRN not found with id " + grnId));

        // Only PENDING can be cancelled
        if (!"PENDING".equalsIgnoreCase(grn.getGrnstatus().getName())) {
            throw new InvalidStateTransitionException("Only PENDING GRNs can be cancelled");
        }

        GrnStatus cancelledStatus = grnStatusRepository.findByName("Cancelled")
                .orElseThrow(() -> new IllegalStateException("CANCELLED status not found"));

        grnStateTransitionHandler.transitionTo(grn, cancelledStatus);

        // No stock update for cancelled GRN
        Grn saved = grnRepository.save(grn);

        return grnMapper.toDto(saved);
    }

}
