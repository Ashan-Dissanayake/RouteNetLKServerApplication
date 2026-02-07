package lk.ashan.routenetlkserverapllication.module.permit.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.permit.dto.PermitDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.mapper.PermitMapper;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PermitService {

    private final PermitRepository permitRepository;
    private final PermitMapper permitMapper;


    public List<PermitDetailResponseDto> getPermits(){
        return permitMapper.toDtoList(permitRepository.findAll());
    }
    public List<PermitDetailResponseDto> searchPermit(@NotNull HashMap<String, String> params) {

        String number = params.get("ssnumber");
        String permitStatusId = params.get("sspermitstatus");
        String routeId = params.get("ssroute");

        Stream<Permite> permitStream = permitRepository.findAll().stream();

        if (number != null) permitStream = permitStream.filter(v->v.getNumber().equalsIgnoreCase(number));
        if (permitStatusId != null) permitStream = permitStream.filter(v->v.getPermitestatus().getId()==Integer.parseInt(permitStatusId));
        if (routeId != null)
            permitStream = permitStream.filter(v -> v.getRoute().getId() == Integer.parseInt(routeId));

        return permitMapper.toDtoList(permitStream.collect(Collectors.toList()));

    }

}
