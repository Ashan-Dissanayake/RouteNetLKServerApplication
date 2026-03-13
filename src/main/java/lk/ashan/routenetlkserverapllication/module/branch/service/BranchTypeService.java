package lk.ashan.routenetlkserverapllication.module.branch.service;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchtypeDto;
import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchtypeMapper;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchtypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchTypeService {

    private final BranchtypeRepository branchtypeRepository;
    private final BranchtypeMapper branchtypeMapper;

    public List<BranchtypeDto> getBranchtypes() {
        return branchtypeMapper.toDtoList(branchtypeRepository.findAll());
    }
}
