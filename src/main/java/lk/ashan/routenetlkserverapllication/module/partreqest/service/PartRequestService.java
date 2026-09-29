package lk.ashan.routenetlkserverapllication.module.partreqest.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchService;
import lk.ashan.routenetlkserverapllication.module.grn.event.GrnProcessedEvent;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnPartRequestItemRepository;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestSummaryDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.mapper.PartRequestItemMapper;
import lk.ashan.routenetlkserverapllication.module.partreqest.mapper.PartRequestMapper;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestItem;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestStatus;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestRepository;
import lk.ashan.routenetlkserverapllication.module.partreqest.validation.PartRequestValidator;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.NumberGeneratorService;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableBranchFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PartRequestService {

    private final PartRequestRepository partRequestRepository;
    private final PartRequestStatusService partRequestStatusService;
    private final NumberGeneratorService numberGeneratorService;
    private final BranchService branchService;
    private final PartRequestMapper partRequestMapper;
    private final PartRequestItemMapper partRequestItemMapper;
    private final GrnPartRequestItemRepository grnPartRequestItemRepository;
    private final PartRequestValidator partRequestValidator;


    @Transactional(readOnly = true)
    public List<PartRequestDetailResponseDto> getPartRequests(){
        return partRequestMapper.toDtoList(partRequestRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<PartRequestDetailResponseDto> searchPartRequests(
            @NotNull HashMap<String, String> params) {

        Specification<PartRequest> specification = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            String requestNumber = params.get("ssnumber");
            String partRequestStatusId = params.get("sspartrequeststatus");

            if (requestNumber != null && !requestNumber.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("number")),
                                requestNumber.toLowerCase()
                        )
                );
            }

            if (partRequestStatusId != null && !partRequestStatusId.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("partrequeststatus").get("id"),
                                Integer.parseInt(partRequestStatusId)
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };

        List<PartRequest> partRequests =
                partRequestRepository.findAll(specification);

        return partRequestMapper.toDtoList(partRequests);
    }

    @Transactional(readOnly = true)
    public PartRequest getById(Integer id) {
        return partRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Request not found with id " + id
                ));
    }

    @Transactional(readOnly = true)
    public List<PartRequestSummaryDto> getSummaryPartRequests() {
        return partRequestMapper.toSummaryDtoList(partRequestRepository.findAll());
    }


    @Transactional
    @DisableBranchFilter
    public PartRequestDetailResponseDto createRequest(
            @NotNull PartRequestCreateRequestDto dto
    ) {

        partRequestValidator.validateCreate(dto);

        PartRequest request = partRequestMapper.toEntity(dto);

        PartRequestStatus initialStatus =
                partRequestStatusService.getByName(
                        request.getPartrequeststatus().getName()
                );

        partRequestValidator.validateInitialStatus(initialStatus);

        request.setPartrequeststatus(initialStatus);

        Branch branch =
                branchService.getById(
                        request.getBranch().getId()
                );

        request.setNumber(
                numberGeneratorService.nextPartRequestNumber(
                        branch.getCode(),
                        YearMonth.now()
                )
        );

        if (request.getPartrequestitems() != null) {
            request.getPartrequestitems()
                    .forEach(item ->
                            item.setPartrequest(request)
                    );
        }

        PartRequest saved = partRequestRepository.save(request);

        return partRequestMapper.toDto(saved);
    }


    @Transactional
    public PartRequestDetailResponseDto updateRequest(@NotNull PartRequestUpdateRequestDto dto) {

        PartRequest request =
                partRequestRepository.findById(dto.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Request not found with id "
                                                + dto.getId()
                                )
                        );

        partRequestValidator.validateUpdate(request, dto);

        partRequestMapper.updateEntity(request, dto);

        request.getPartrequestitems().clear();

        dto.getPartrequestitems()
                .forEach(itemDto -> {

                    PartRequestItem item = partRequestItemMapper.toEntity(itemDto);

                    item.setPartrequest(request);

                    request.getPartrequestitems().add(item);
                });

        PartRequest saved = partRequestRepository.save(request);

        return partRequestMapper.toDto(saved);
    }

    @Transactional
    public PartRequestDetailResponseDto approveRequest(@NotNull Integer id) {

        PartRequest request =
                partRequestRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Request not found with id " + id
                                )
                        );

        PartRequestStatus approvedStatus = partRequestStatusService.getByName("Approved");

        changeStatus(request, approvedStatus);

        return partRequestMapper.toDto(request);
    }

    @Transactional
    public PartRequestDetailResponseDto rejectRequest(@NotNull Integer id) {

        PartRequest request =
                partRequestRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Request not found with id " + id
                                )
                        );

        PartRequestStatus rejectedStatus = partRequestStatusService.getByName("Rejected");

        changeStatus(request, rejectedStatus);

        return partRequestMapper.toDto(request);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGrnProcessed(GrnProcessedEvent event) {

        PartRequest request =
                partRequestRepository.findById(
                                event.partRequestId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Part Request not found"
                                )
                        );

        boolean isFullyReceived =
                request.getPartrequestitems()
                        .stream()
                        .allMatch(item -> {

                            BigDecimal totalReceived =
                                    grnPartRequestItemRepository
                                            .sumQuantityByPartRequestItemId(
                                                    item.getId(),
                                                    List.of(
                                                            "Received",
                                                            "Partially Received"
                                                    )
                                            );

                            BigDecimal received =
                                    totalReceived != null
                                            ? totalReceived
                                            : BigDecimal.ZERO;

                            return received.compareTo(
                                    item.getQuantity()
                            ) >= 0;
                        });

        if (isFullyReceived) {

            PartRequestStatus completedStatus =
                    partRequestStatusService.getByName(
                            "Completed"
                    );

            changeStatus(request, completedStatus);

            partRequestRepository.saveAndFlush(request);
        }
    }


    private void changeStatus(PartRequest request, PartRequestStatus targetStatus) {
        PartRequestStatus currentStatus = request.getPartrequeststatus();

        partRequestValidator.validateStatusTransition(currentStatus, targetStatus);

        request.setPartrequeststatus(targetStatus);
    }

}
