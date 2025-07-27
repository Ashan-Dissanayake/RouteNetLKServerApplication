package lk.ashan.ntcserverapllication.module.branch.service;

import lk.ashan.ntcserverapllication.module.branch.dto.BranchtypeResponse;
import lk.ashan.ntcserverapllication.module.branch.mapper.BranchtypeMapper;
import lk.ashan.ntcserverapllication.module.branch.repository.BranchtypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchtypeService {

    private final BranchtypeRepository branchtypeRepository;
    private final BranchtypeMapper branchtypeMapper;

    public List<BranchtypeResponse> getBranchtypes() {
        return branchtypeMapper.toBranchtypeResponseList(branchtypeRepository.findAll());
    }
}
