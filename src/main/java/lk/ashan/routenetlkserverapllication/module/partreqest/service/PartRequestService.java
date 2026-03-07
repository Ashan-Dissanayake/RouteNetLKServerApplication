package lk.ashan.routenetlkserverapllication.module.partreqest.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.mapper.PartRequestMapper;
import lk.ashan.routenetlkserverapllication.module.partreqest.mapper.PartRequestStatusMapper;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestRepository;
import lk.ashan.routenetlkserverapllication.module.sparepart.dto.PartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PartRequestService {

    private final PartRequestRepository partRequestRepository;
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

}
