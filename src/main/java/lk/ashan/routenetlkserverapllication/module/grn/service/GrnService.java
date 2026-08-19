package lk.ashan.routenetlkserverapllication.module.grn.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.grn.event.PartReceivedEvent;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.grn.mapper.GrnMapper;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnPartRequestItemDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnPartRequestItem;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnRepository;
import lk.ashan.routenetlkserverapllication.module.grn.validation.GrnContext;
import lk.ashan.routenetlkserverapllication.module.grn.validation.GrnProcessingStrategy;
import lk.ashan.routenetlkserverapllication.module.partreqest.event.PartRequestApprovedEvent;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.service.PartRequestService;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.numbergenerator.NumberGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for managing GRNs (Goods Receipt Notes).
 * Provides methods for retrieving, searching, creating, and updating GRNs.
 */
@Service
@RequiredArgsConstructor
public class GrnService {

    private final GrnRepository grnRepository;
    private final PartRequestService partRequestService;
    private final GrnStatusService grnStatusService;
    private final NumberGeneratorService numberGeneratorService;
    private final GrnMapper grnMapper;
    private final ApplicationEventPublisher eventPublisher;

    private final List<GrnProcessingStrategy> strategies;

    /**
     * Retrieves all GRNs.
     *
     * @return a list of {@link GrnDetailResponseDto} representing all GRNs.
     */
    @Transactional(readOnly = true)
    public List<GrnDetailResponseDto> getGrns() {
        return grnMapper.toDtoList(grnRepository.findAll());
    }

    /**
     * Searches for GRNs based on the provided parameters.
     *
     * @param params a map of search parameters, including "ssnumber", "sspartrequest", and "ssgrnstatus".
     * @return a list of {@link GrnDetailResponseDto} matching the search criteria.
     */
    @Transactional(readOnly = true)
    public List<GrnDetailResponseDto> searchGrns(@NotNull HashMap<String, String> params) {

        List<Grn> grns = grnRepository.findAll();

        String number = params.get("ssnumber");
        String partRequestId = params.get("sspartrequest");
        String grnStatusId = params.get("ssgrnstatus");

        Stream<Grn> grnStream = grns.stream();

        if (number != null) grnStream = grnStream.filter(r -> r.getNumber().equals(number));
        if (partRequestId != null) grnStream = grnStream.filter(r -> r.getPartrequest().getId() == Integer.parseInt(partRequestId));
        if (grnStatusId != null) grnStream = grnStream.filter(r -> r.getGrnstatus().getId() == Integer.parseInt(grnStatusId));

        return grnMapper.toDtoList(grnStream.collect(Collectors.toList()));
    }

    /**
     * Handles the event when a part request is approved.
     * Creates a draft GRN for the approved part request.
     *
     * @param event the {@link PartRequestApprovedEvent} containing the part request ID.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePartRequestApproved(PartRequestApprovedEvent event) {
        this.createDraftGrn(event.partRequestId());
    }

    /**
     * Updates an existing GRN based on the provided update request.
     *
     * @param dto the {@link GrnUpdateRequestDto} containing the update details.
     * @return the updated {@link GrnDetailResponseDto}.
     * @throws ResourceNotFoundException if the GRN with the specified ID is not found.
     * @throws InvalidStateTransitionException if the GRN is not in a "DRAFT" state.
     * @throws BusinessRuleViolationException if there is an item ID mismatch for GRN parts.
     */
    @Transactional
    public GrnDetailResponseDto updateGrn(@NotNull GrnUpdateRequestDto dto) {
        // 1. Fetch
        Grn existing = grnRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("GRN not found with id " + dto.getId()));

        // 2. Validate
        if (!"DRAFT".equalsIgnoreCase(existing.getGrnstatus().getName())) {
            throw new InvalidStateTransitionException("Only DRAFT GRNs can be updated.");
        }

        // 3. Map Header
        grnMapper.updateEntityFromDto(existing, dto);

        // 4. Update Items and trigger Stock
        BigDecimal totalReceivedNow = BigDecimal.ZERO;
        BigDecimal totalExpectedInThisDraft = BigDecimal.ZERO;

        for (GrnPartRequestItemDto itemDto : dto.getGrnpartrequestitems()) {
            GrnPartRequestItem existingItem = existing.getGrnpartrequestitems().stream()
                    .filter(item -> item.getId().equals(itemDto.getId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessRuleViolationException("Item ID mismatch for GRN Part"));

            // Track what we expected for THIS draft specifically
            totalExpectedInThisDraft = totalExpectedInThisDraft.add(existingItem.getQuantity());

            BigDecimal actualReceived = itemDto.getQuantity();
            totalReceivedNow = totalReceivedNow.add(actualReceived);

            // Update the physical receipt quantity
            existingItem.setQuantity(actualReceived);

            // Announce Stock Update
            eventPublisher.publishEvent(new PartReceivedEvent(
                    existingItem.getGrn().getBranch(),
                    existingItem.getPartrequestitem().getPart().getId(),
                    actualReceived
            ));
        }

        // !! IMPORTANT: Flush to DB so the Factory's SUM query sees these new quantities !!
        grnRepository.saveAndFlush(existing);

        // 5. Build Context for Strategy
        GrnContext context = GrnContext.builder()
                .grn(existing)
                .partRequestId(existing.getPartrequest().getId())
                .receivedQty(totalReceivedNow)
                .expectedQty(totalExpectedInThisDraft)
                .build();

        // 6. Execute Strategy
        strategies.stream()
                .filter(s -> s.isApplicable(context.getReceivedQty(), context.getExpectedQty()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Critical: No strategy found for the given quantities"))
                .process(context);

        // Final save (Strategy might have changed the status)
        return grnMapper.toDto(grnRepository.save(existing));
    }

    /**
     * Creates a draft GRN for the specified part request.
     *
     * @param partRequestId the ID of the part request.
     */
    private void createDraftGrn(@NotNull Integer partRequestId) {
        PartRequest partRequest = partRequestService.getById(partRequestId);

        Grn grn = new Grn();
        grn.setPartrequest(partRequest);
        grn.setBranch(partRequest.getBranch());

        String grnNumber = numberGeneratorService.nextGrnNumber(partRequest.getBranch().getCode(), YearMonth.now());

        grn.setNumber(grnNumber);

        GrnStatus draftStatus = grnStatusService.getByName("Draft");
        grn.setGrnstatus(draftStatus);

        List<GrnPartRequestItem> grnPartRequestItems = partRequest.getPartrequestitems().stream().map(item -> {
            GrnPartRequestItem grnPart = new GrnPartRequestItem();
            grnPart.setGrn(grn);
            grnPart.setPartrequestitem(item); // Link to the M:N associative entity
            grnPart.setQuantity(item.getQuantity()); // Default to full expected qty
            return grnPart;
        }).collect(Collectors.toList());

        grn.setGrnpartrequestitems(grnPartRequestItems);

        Grn saved = grnRepository.save(grn);
        grnMapper.toDto(saved);
    }
}
