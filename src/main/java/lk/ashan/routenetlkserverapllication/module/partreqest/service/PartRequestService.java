package lk.ashan.routenetlkserverapllication.module.partreqest.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchService;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.mapper.PartRequestItemMapper;
import lk.ashan.routenetlkserverapllication.module.partreqest.mapper.PartRequestMapper;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestItem;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestStatus;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestRepository;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestStatusRepository;
import lk.ashan.routenetlkserverapllication.module.partreqest.state.PartRequestState;
import lk.ashan.routenetlkserverapllication.module.partreqest.state.PartRequestStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.partreqest.state.PartRequestStatusFactory;
import lk.ashan.routenetlkserverapllication.module.partreqest.validation.PartRequestValidationContext;
import lk.ashan.routenetlkserverapllication.module.partreqest.validation.PartRequestValidationContextBuilder;
import lk.ashan.routenetlkserverapllication.module.partreqest.validation.PartRequestValidationStrategy;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.NumberGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartRequestService {

    private final PartRequestRepository partRequestRepository;
    private final PartRequestStatusRepository partRequestStatusRepository;
    private final PartRequestStatusService partRequestStatusService;
    private final NumberGeneratorService numberGeneratorService;
    private final BranchService branchService;
    private final PartRequestMapper partRequestMapper;
    private final PartRequestItemMapper partRequestItemMapper;

    private final PartRequestValidationContextBuilder contextBuilder;
    private final List<PartRequestValidationStrategy> validationStrategies;
    private final PartRequestStateTransitionHandler partRequestStateTransitionHandler;
    private final PartRequestStatusFactory partRequestStatusFactory;


    @Transactional(readOnly = true)
    public List<PartRequestDetailResponseDto> getPartRequests(){
        return partRequestMapper.toDtoList(partRequestRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<PartRequestDetailResponseDto> searchPartRequests(@NotNull HashMap<String, String> params) {

        List<PartRequest> partRequests = partRequestRepository.findAll();

        String requestNumber = params.get("ssnumber");
        String partRequestStatusId= params.get("sspartrequeststatus");

        Stream<PartRequest> partRequestStream = partRequests.stream();

        if(requestNumber!=null)partRequestStream = partRequestStream.filter(r->r.getNumber().equalsIgnoreCase(requestNumber));
        if(partRequestStatusId!=null)partRequestStream = partRequestStream.filter(r->r.getPartrequeststatus().getId()==Integer.parseInt(partRequestStatusId));

        return partRequestMapper.toDtoList( partRequestStream.collect(Collectors.toList()));
    }

    @Transactional
    public PartRequestDetailResponseDto createRequest(@NotNull PartRequestCreateRequestDto dto) {

        PartRequestValidationContext context = contextBuilder.buildForCreate(dto);

        validationStrategies.forEach(strategy -> strategy.validate(context));

        PartRequest request = partRequestMapper.toEntity(dto);

        PartRequestStatus initialStatus = partRequestStatusService.getByName(request.getPartrequeststatus().getName());
        PartRequestState initialState = partRequestStatusFactory.getState(initialStatus.getName());
        initialState.validateInitial();
        request.setPartrequeststatus(initialStatus);

        Branch branch = branchService.getById(request.getBranch().getId());

        request.setNumber(numberGeneratorService.nextPartRequestNumber(branch.getCode(), YearMonth.now()));

        request.getPartrequestitems()
                .forEach(i ->
                        log.info("Item id before save: {}", i.getId())
                );

        if (request.getPartrequestitems() != null) {
            request.getPartrequestitems()
                    .forEach(item -> item.setPartrequest(request));
        }

        PartRequest saved = partRequestRepository.save(request);
        return partRequestMapper.toDto(saved);
    }

    @Transactional
    public PartRequestDetailResponseDto updateRequest(@NotNull PartRequestUpdateRequestDto dto) {

        PartRequest request = partRequestRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Request not found with id " + dto.getId()
                ));

        String currentStatus = request.getPartrequeststatus().getName();

        if (!"PENDING".equalsIgnoreCase(currentStatus)) {
            throw new InvalidStateTransitionException(
                    "Only PENDING requests can be updated"
            );
        }

        if (dto.getPartrequestitems() == null || dto.getPartrequestitems().isEmpty()) {
            throw new BusinessRuleViolationException(
                    "Request must contain at least one part"
            );
        }

        dto.getPartrequestitems().forEach(item -> {
            if (item.getQuantity() == null ||
                    item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessRuleViolationException(
                        "Requested quantity must be greater than zero"
                );
            }
        });

        partRequestMapper.updateEntity(request, dto);

        request.getPartrequestitems().clear();

        dto.getPartrequestitems().forEach(itemDto -> {
            PartRequestItem item = partRequestItemMapper.toEntity(itemDto);
            item.setPartrequest(request);
            request.getPartrequestitems().add(item);
        });

        PartRequest saved = partRequestRepository.save(request);

        return partRequestMapper.toDto(saved);
    }

    @Transactional
    public PartRequestDetailResponseDto approveRequest(@NotNull Integer id) {

        PartRequest request = partRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Request not found with id " + id
                ));

        PartRequestStatus approvedStatus = partRequestStatusRepository
                .findByName("Approved")
                .orElseThrow(() -> new IllegalStateException(
                        "Status APPROVED not found"
                ));

        partRequestStateTransitionHandler.transitionTo(request, approvedStatus);

        return partRequestMapper.toDto(request);
    }

    @Transactional
    public PartRequestDetailResponseDto rejectRequest(@NotNull Integer id) {

        PartRequest request = partRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Request not found with id " + id
                ));

        PartRequestStatus rejectedStatus = partRequestStatusRepository
                .findByName("Rejected")
                .orElseThrow(() -> new IllegalStateException(
                        "Status REJECTED not found"
                ));

        partRequestStateTransitionHandler.transitionTo(request, rejectedStatus);

        return partRequestMapper.toDto(request);
    }

    @Transactional
    public PartRequestDetailResponseDto completeRequest(@NotNull Integer id) {

        PartRequest request = partRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Request not found with id " + id
                ));

        PartRequestStatus completedStatus = partRequestStatusRepository
                .findByName("Completed")
                .orElseThrow(() -> new IllegalStateException(
                        "Status COMPLETED not found"
                ));

        partRequestStateTransitionHandler.transitionTo(request, completedStatus);

        return partRequestMapper.toDto(request);
    }

}
