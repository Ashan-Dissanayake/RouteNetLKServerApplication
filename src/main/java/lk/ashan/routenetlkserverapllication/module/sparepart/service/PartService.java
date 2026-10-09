package lk.ashan.routenetlkserverapllication.module.sparepart.service;

import jakarta.persistence.criteria.Predicate;
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
import lk.ashan.routenetlkserverapllication.module.sparepart.validation.PartValidator;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PartService {

    private final PartRepository partRepository;
    private final PartStatusService partStatusService;
    private final PartMasterService partMasterService;
    private final BranchService branchService;
    private final PartMapper partMapper;
    private final PartValidator partValidator;
    private final PartStatusRepository partStatusRepository;


    @Transactional(readOnly = true)
    public List<PartDetailResponseDto> getParts() {
        return partMapper.toDtoList(partRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<PartDetailResponseDto> searchParts(@NotNull HashMap<String, String> params) {

        Specification<Part> specification =
                (root, query, criteriaBuilder) -> {

                    List<Predicate> predicates =
                            new ArrayList<>();

                    String partCategoryId = params.get("sscategory");
                    String partStatusId = params.get("sspartstatus");

                    if (partCategoryId != null && !partCategoryId.isBlank()) {
                        predicates.add(
                                criteriaBuilder.equal(
                                        root.get("partmaster")
                                                .get("partcategory")
                                                .get("id"),
                                        Integer.parseInt(partCategoryId)
                                )
                        );
                    }

                    if (partStatusId != null && !partStatusId.isBlank()) {
                        predicates.add(
                                criteriaBuilder.equal(
                                        root.get("partstatus").get("id"),
                                        Integer.parseInt(partStatusId)
                                )
                        );
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };

        List<Part> parts = partRepository.findAll(specification);

        return partMapper.toDtoList(parts);
    }

    @Transactional(readOnly = true)
    public List<PartSummaryDto> getSummaryParts() {
        return partMapper.toSummaryDtoList(partRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Part getById(Integer id) {
        return partRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Part not found"
                        )
                );
    }

    @Transactional
    @DisableSoftDeleteFilter
    public PartDetailResponseDto createPart(@NotNull PartCreateRequestDto dto) {

        partValidator.validateCreate(dto);

        Partstatus initialStatus = partStatusService.getByName(dto.getPartstatus().getName());

        partValidator.validateInitialStatus(initialStatus);

        Part part = partMapper.toEntity(dto);
        part.setPartstatus(initialStatus);

        Part saved = partRepository.save(part);

        return partMapper.toDto(saved);
    }

    @Transactional
    @DisableSoftDeleteFilter
    public PartDetailResponseDto updatePart(@NotNull PartUpdateRequestDto dto) {

        Part existingPart =
                partRepository.findById(dto.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Part not found"
                                )
                        );

        partValidator.validateUpdate(dto, existingPart);
        partMapper.updateFromDto(dto, existingPart);

        if (dto.getBranch() != null && dto.getBranch().getId() != null) {

            Branch targetBranch =
                    branchService.getById(
                            dto.getBranch().getId()
                    );
            existingPart.setBranch(targetBranch);
        }

        if (dto.getPartmaster() != null && dto.getPartmaster().getId() != null) {

            Partmaster targetPartMaster =
                    partMasterService.getById(
                            dto.getPartmaster().getId()
                    );

            existingPart.setPartmaster(targetPartMaster);
        }

        if (dto.getPartstatus() != null && dto.getPartstatus().getId() != null) {

            Partstatus targetStatus =
                    partStatusService.getById(
                            dto.getPartstatus().getId()
                    );

            changeStatus(existingPart, targetStatus);
        }

        Part saved = partRepository.save(existingPart);
        return partMapper.toDto(saved);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlePartReceived(PartReceivedEvent event) {

        Part part =
                partRepository.findById(event.partId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Part not found"
                                )
                        );

        BigDecimal currentQoh =
                part.getQoh() != null
                        ? part.getQoh()
                        : BigDecimal.ZERO;

        part.setQoh(currentQoh.add(event.quantityReceived()));

        updatePartStatus(part);

        partRepository.save(part);
    }

    @Transactional
    public List<Integer> deactivateParts(List<Integer> partIds) {

        List<Part> parts = partRepository.findAllById(partIds);

        if (parts.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No parts found for the given IDs: "
                            + partIds
            );
        }

        partValidator.validateDeactivation(parts);

        parts.forEach(part -> part.setDeleted(true));

        partRepository.saveAll(parts);

        return parts.stream()
                .map(Part::getId)
                .toList();
    }

    private void updatePartStatus(Part part) {

        String currentStatusName =
                part.getPartstatus()
                        .getName()
                        .trim()
                        .toUpperCase();

        if ("DECOMMISSIONED".equals(
                currentStatusName
        )) {
            return;
        }

        String targetStatusName =
                calculateTargetStatus(
                        part.getQoh(),
                        part.getRop()
                );

        if (currentStatusName.equalsIgnoreCase(targetStatusName)) {
            return;
        }

        Partstatus targetStatus =
                partStatusRepository.findByName(
                                targetStatusName
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Part status not found: "
                                                + targetStatusName
                                )
                        );

        changeStatus(part, targetStatus);
    }

    private void changeStatus(Part part, Partstatus targetStatus) {
        partValidator.validateStatusTransition(part.getPartstatus(), targetStatus);
        part.setPartstatus(targetStatus);
    }

    private String calculateTargetStatus(BigDecimal qoh, BigDecimal rop) {

        if (qoh.compareTo(BigDecimal.ZERO) <= 0) {
            return "OUT OF STOCK";
        }

        if (qoh.compareTo(rop) <= 0) {
            return "LOW STOCK";
        }

        return "AVAILABLE";
    }
}