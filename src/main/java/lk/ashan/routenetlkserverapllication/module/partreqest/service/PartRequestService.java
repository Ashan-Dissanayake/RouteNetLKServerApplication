package lk.ashan.routenetlkserverapllication.module.partreqest.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.mapper.PartRequestMapper;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequeststatus;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestRepository;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestStatusRepository;
import lk.ashan.routenetlkserverapllication.module.partreqest.state.PartRequestState;
import lk.ashan.routenetlkserverapllication.module.partreqest.state.PartRequestStatusFactory;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
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
public class PartRequestService {

    private final PartRequestRepository partRequestRepository;
    private final PartRequestStatusRepository partRequestStatusRepository;
    private final PartRequestStatusFactory partRequestStatusFactory;
    private final PartRequestMapper partRequestMapper;

    @Transactional(readOnly = true)
    public List<PartRequestDetailResponseDto> getPartRequests(){
        return partRequestMapper.toDtoList(partRequestRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<PartRequestDetailResponseDto> searchPartRequests(@NotNull HashMap<String, String> params) {

        List<Partrequest> partRequests = partRequestRepository.findAll();

        String requestNumber = params.get("ssnumber");
        String partRequestStatusId= params.get("sspartrequeststatus");

        Stream<Partrequest> partRequestStream = partRequests.stream();

        if(requestNumber!=null)partRequestStream = partRequestStream.filter(r->r.getNumber().equals(requestNumber));
        if(partRequestStatusId!=null)partRequestStream = partRequestStream.filter(r->r.getPartrequeststatus().getId()==Integer.parseInt(partRequestStatusId));

        return partRequestMapper.toDtoList( partRequestStream.collect(Collectors.toList()));
    }

    @Transactional
    public PartRequestDetailResponseDto createRequest(@NotNull PartRequestCreateRequestDto dto) {

        Partrequest request = partRequestMapper.toEntity(dto);

        if (dto.getPartrequestitems() == null || dto.getPartrequestitems().isEmpty()) {
            throw new BusinessRuleViolationException(
                    "Request must contain at least one part"
            );
        }

        dto.getPartrequestitems().forEach(item -> {
            if (item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessRuleViolationException(
                        "Requested quantity must be greater than zero"
                );
            }
        });

        Partrequeststatus initialStatus = partRequestStatusRepository
                .findByName("Pending")
                .orElseThrow(() -> new IllegalStateException("Initial status PENDING not found"));

        PartRequestState initialState =
                partRequestStatusFactory.getState(initialStatus.getName());

        initialState.validateInitial();

        request.setPartrequeststatus(initialStatus);

        Partrequest saved = partRequestRepository.save(request);

        return partRequestMapper.toDto(saved);
    }


}
