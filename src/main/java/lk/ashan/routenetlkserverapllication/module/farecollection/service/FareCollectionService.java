package lk.ashan.routenetlkserverapllication.module.farecollection.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FareCollectionService {

    private final FareCollectionRepository fareCollectionRepository;
    private final BranchRepository branchRepository;
    private final FareCollectionMapper fareCollectionMapper;

    private final FareCollectionContextBuilder contextBuilder;
    private final FareCollectionCreationValidationStrategy creationValidationStrategy;

    @Transactional(readOnly = true)
    public List<FareCollectionDetailResponseDto> getFareCollections(){
        return fareCollectionMapper.toDtoList(fareCollectionRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<FareCollectionDetailResponseDto> searchFareCollections(@NotNull HashMap<String, String> params) {

        List<FareCollection> fareCollections = fareCollectionRepository.findAll();

        String tripExecutionId = params.get("sstripexecution");
        String ticketMachineId= params.get("ssticketmachine");

        Stream<FareCollection> fareCollectionStream = fareCollections.stream();

        if(tripExecutionId!=null)fareCollectionStream = fareCollectionStream.filter(r->r.getTripexecution().getId() == Integer.parseInt(tripExecutionId));
        if(ticketMachineId!=null)fareCollectionStream = fareCollectionStream.filter(r->r.getTicketmachine().getId()==Integer.parseInt(ticketMachineId));

        return fareCollectionMapper.toDtoList( fareCollectionStream.collect(Collectors.toList()));
    }

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


    @Transactional
    public void  reconciled(@NotNull Integer fareCollectionId) {
        FareCollection execution = fareCollectionRepository.findById(fareCollectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fare Collection not found with id " + fareCollectionId
                ));

        execution.setIsreconciled(true);
    }



}
