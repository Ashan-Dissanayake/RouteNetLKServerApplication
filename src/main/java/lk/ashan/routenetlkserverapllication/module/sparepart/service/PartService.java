package lk.ashan.routenetlkserverapllication.module.sparepart.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchService;
import lk.ashan.routenetlkserverapllication.module.grn.event.PartReceivedEvent;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partmaster;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartRepository;
import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartStatusRepository;
import lk.ashan.routenetlkserverapllication.module.sparepart.state.PartStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.sparepart.state.PartStatusFactory;
import lk.ashan.routenetlkserverapllication.module.sparepart.validation.PartContext;
import lk.ashan.routenetlkserverapllication.module.sparepart.validation.PartContextBuilder;
import lk.ashan.routenetlkserverapllication.module.sparepart.validation.PartCreationStrategy;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PartService {
    
    private final PartRepository partRepository;
    private final PartStatusService partStatusService;
    private final PartMasterService partMasterService;
    private final BranchService branchService;
    private final PartMapper partMapper;

    private final List<PartCreationStrategy> partCreationStrategies;
    private final PartStatusFactory partStatusFactory;
    private final PartStatusRepository partStatusRepository;
    private final PartStateTransitionHandler partStateTransitionHandler;
    private final PartContextBuilder partContextBuilder;


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

        if(partCategoryId!=null)partStream = partStream.filter(r->r.getPartmaster().getPartcategory().getId() == Integer.parseInt(partCategoryId));
        if(partStatusId!=null)partStream = partStream.filter(r->r.getPartstatus().getId()==Integer.parseInt(partStatusId));

        return partMapper.toDtoList( partStream.collect(Collectors.toList()));
    }


    @Transactional(readOnly = true)
    public List<PartSummaryDto> getSummaryParts() {
        return partMapper.toSummaryDtoList(partRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Part getById(Integer id) {
        return partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found"));
    }

    @Transactional
    @DisableSoftDeleteFilter
    public PartDetailResponseDto createPart(@NotNull PartCreateRequestDto dto) {

        boolean exists = partRepository.existsByBranch_IdAndPartmaster_Id(dto.getBranch().getId(), dto.getPartmaster().getId());
        if (exists) {
            throw new ResourceExistsException("This part already exists");
        }

        Part part = partMapper.toEntity(dto);

        PartContext context = partContextBuilder.buildForCreate(dto);
        partCreationStrategies.forEach(strategy -> strategy.validate(context));

        Partstatus initialStatus = partStatusService.getByName(dto.getPartstatus().getName());
        partStatusFactory.getState(initialStatus.getName()).validateInitial();
        part.setPartstatus(initialStatus);

        Part saved = partRepository.save(part);
        return partMapper.toDto(saved);
    }

    @Transactional
    @DisableSoftDeleteFilter
    public PartDetailResponseDto updatePart(@NotNull PartUpdateRequestDto dto) {

        Part existingPart = partRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Part not found"));

        PartContext context = partContextBuilder.buildForUpdate(dto, existingPart);

        partCreationStrategies.forEach(strategy -> strategy.validate(context));

        partMapper.updateFromDto(dto, existingPart);

        if (dto.getBranch() != null && dto.getBranch().getId() != null) {
            Branch targetBranch = branchService.getById(dto.getBranch().getId());
            existingPart.setBranch(targetBranch);
        }

        if (dto.getPartmaster() != null && dto.getPartmaster().getId() != null) {
            Partmaster targetPartMaster = partMasterService.getById(dto.getPartmaster().getId());
            existingPart.setPartmaster(targetPartMaster);
        }

        if (dto.getPartstatus() != null && dto.getPartstatus().getId() != null) {
            Partstatus targetStatus = partStatusService.getById(dto.getPartstatus().getId());
            partStateTransitionHandler.transitionTo(existingPart, targetStatus);
        }

        Part saved = partRepository.save(existingPart);

        return partMapper.toDto(saved);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlePartReceived(PartReceivedEvent event) {
        Part part = partRepository.findById(event.partId())
                .orElseThrow(() -> new ResourceNotFoundException("Part not found"));

        // 1. Update the actual quantity
        BigDecimal currentQoh = part.getQoh() != null ? part.getQoh() : BigDecimal.ZERO;
        part.setQoh(currentQoh.add(event.quantityReceived()));

        // 2. Determine and Transition the Status
        updatePartStatus(part);

        partRepository.save(part);
    }

    @Transactional
    public List<Integer> deactivateParts(List<Integer> partIds) {

        List<Part> parts = partRepository.findAllById(partIds);

        if (parts.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No parts found for the given IDs: " + partIds
            );
        }

        validateNotDecommissioned(parts);

        parts.forEach(part -> part.setDeleted(true));

        partRepository.saveAll(parts);

        return parts.stream()
                .map(Part::getId)
                .toList();
    }

    private void validateNotDecommissioned(List<Part> parts) {

        parts.stream()
                .filter(part ->
                        "DECOMMISSIONED".equalsIgnoreCase(
                                part.getPartstatus().getName()
                        )
                )
                .findFirst()
                .ifPresent(part -> {
                    throw new BusinessRuleViolationException(
                            String.format(
                                    "%s parts cannot be deleted. Part ID: %d",
                                    part.getPartstatus().getName(),
                                    part.getId()
                            )
                    );
                });
    }

    private void updatePartStatus(Part part) {
        String currentStatusName = part.getPartstatus().getName().toUpperCase();

        // Safety: If it's already DECOMMISSIONED, the Handler/State will throw an
        // exception if we try to move it. We catch it or check here to prevent crashes.
        if ("DECOMMISSIONED".equals(currentStatusName)) {
            return;
        }

        // 1. Calculate what the status SHOULD be based on QOH
        String targetStatusName = calculateTargetStatus(part.getQoh(), part.getRop());

        // 2. Only transition if the status actually needs to change
        if (!currentStatusName.equalsIgnoreCase(targetStatusName)) {
            Partstatus newStatus = partStatusRepository.findByName(targetStatusName)
                    .orElseThrow(() -> new IllegalStateException("Status " + targetStatusName + " not found"));

            // 3. USE YOUR HANDLER: This triggers transitionTo -> currentState.transitionTo -> executeOnEnter
            partStateTransitionHandler.transitionTo(part, newStatus);
        }
    }

    private String calculateTargetStatus(BigDecimal qoh, BigDecimal rop) {
        if (qoh.compareTo(BigDecimal.ZERO) <= 0) return "OUT OF STOCK";
        if (qoh.compareTo(rop) <= 0) return "LOW STOCK";
        return "AVAILABLE";
    }

}
