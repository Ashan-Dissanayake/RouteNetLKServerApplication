package lk.ashan.routenetlkserverapllication.module.farecollection.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.farecollection.event.FareReconciledEvent;
import lk.ashan.routenetlkserverapllication.module.farecollection.mapper.FareCollectionMapper;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.FareCollectionCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.FareCollectionDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.FareCollection;
import lk.ashan.routenetlkserverapllication.module.farecollection.repository.FareCollectionRepository;
import lk.ashan.routenetlkserverapllication.module.farecollection.validation.FareCollectionContextBuilder;
import lk.ashan.routenetlkserverapllication.module.farecollection.validation.FareCollectionCreationValidationStrategy;
import lk.ashan.routenetlkserverapllication.module.farecollection.validation.FareCollectionValidationContext;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for managing fare collections.
 * Provides methods for retrieving, searching, creating, and reconciling fare collections.
 */
@Service
@RequiredArgsConstructor
public class FareCollectionService {

    private final FareCollectionRepository fareCollectionRepository;
    private final BranchRepository branchRepository;
    private final FareCollectionMapper fareCollectionMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final FareCollectionContextBuilder contextBuilder;
    private final FareCollectionCreationValidationStrategy creationValidationStrategy;

    /**
     * Retrieves all fare collections.
     *
     * @return a list of {@link FareCollectionDetailResponseDto} containing details of all fare collections.
     */
    @Transactional(readOnly = true)
    public List<FareCollectionDetailResponseDto> getFareCollections() {
        return fareCollectionMapper.toDtoList(fareCollectionRepository.findAll());
    }

    /**
     * Searches for fare collections based on the provided parameters.
     *
     * @param params a map of search parameters, including "sstripexecution" for trip execution ID
     *               and "ssticketmachine" for ticket machine ID.
     * @return a list of {@link FareCollectionDetailResponseDto} matching the search criteria.
     */
    @Transactional(readOnly = true)
    public List<FareCollectionDetailResponseDto> searchFareCollections(@NotNull HashMap<String, String> params) {

        List<FareCollection> fareCollections = fareCollectionRepository.findAll();

        String tripExecutionId = params.get("sstripexecution");
        String ticketMachineId = params.get("ssticketmachine");

        Stream<FareCollection> fareCollectionStream = fareCollections.stream();

        if (tripExecutionId != null)
            fareCollectionStream = fareCollectionStream.filter(r -> r.getTripexecution().getId() == Integer.parseInt(tripExecutionId));
        if (ticketMachineId != null)
            fareCollectionStream = fareCollectionStream.filter(r -> r.getTicketmachine().getId() == Integer.parseInt(ticketMachineId));

        return fareCollectionMapper.toDtoList(fareCollectionStream.collect(Collectors.toList()));
    }

    /**
     * Creates a new fare collection.
     *
     * @param request the {@link FareCollectionCreateRequestDto} containing details for the new fare collection.
     * @return the created {@link FareCollectionDetailResponseDto}.
     * @throws ResourceNotFoundException if the branch specified in the request is not found.
     */
    @Transactional
    public FareCollectionDetailResponseDto createFareCollection(FareCollectionCreateRequestDto request) {
        branchRepository.findById(request.getBranch().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        FareCollectionValidationContext context = contextBuilder.build(request);
        creationValidationStrategy.validate(context);

        FareCollection fareCollection = fareCollectionMapper.toEntity(request);
        fareCollection.setIsreconciled(false);
        fareCollection.setTocollected(LocalTime.now());

        FareCollection saved = fareCollectionRepository.save(fareCollection);

        return fareCollectionMapper.toDto(saved);
    }

    /**
     * Marks a fare collection as reconciled and publishes a reconciliation event.
     *
     * @param fareCollectionId the ID of the fare collection to reconcile.
     * @throws ResourceNotFoundException if the fare collection with the specified ID is not found.
     */
    @Transactional
    public void reconciled(@NotNull Integer fareCollectionId) {
        FareCollection execution = fareCollectionRepository.findById(fareCollectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fare Collection not found with id " + fareCollectionId
                ));

        execution.setIsreconciled(true);
        eventPublisher.publishEvent(new FareReconciledEvent(fareCollectionId, execution.getBranch()));
    }
}
