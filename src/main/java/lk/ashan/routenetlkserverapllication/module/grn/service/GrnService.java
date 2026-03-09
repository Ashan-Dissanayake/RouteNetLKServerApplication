package lk.ashan.routenetlkserverapllication.module.grn.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.grn.dto.GrnDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.grn.mapper.GrnMapper;
import lk.ashan.routenetlkserverapllication.module.grn.model.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.Grnstatus;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnRepository;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnStatusRepository;
import lk.ashan.routenetlkserverapllication.module.grn.state.GrnState;
import lk.ashan.routenetlkserverapllication.module.grn.state.GrnStatusFactory;
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
public class GrnService {

    private final GrnRepository grnRepository;
    private final GrnStatusRepository grnStatusRepository;

    private final GrnMapper grnMapper;
    private final GrnStatusFactory grnStatusFactory;

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

        Grnstatus initialStatus = grnStatusRepository.findByName("Pending")
                .orElseThrow(() -> new IllegalStateException("Initial status PENDING not found"));

        GrnState initialState = grnStatusFactory.getState(initialStatus.getName());
        initialState.validateInitial();

        grn.setGrnstatus(initialStatus);

        Grn saved = grnRepository.save(grn);

        return grnMapper.toDto(saved);
    }

}
