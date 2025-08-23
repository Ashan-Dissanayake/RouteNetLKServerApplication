package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchtypeResponse;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchtypeMapper;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchtypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchtypeService {

    private final BranchtypeRepository branchtypeRepository;
    private final BranchtypeMapper branchtypeMapper;

    public List<BranchtypeResponse> getBranchtypes() {
        return branchtypeMapper.toBranchtypeResponseList(branchtypeRepository.findAll());
    }
}
