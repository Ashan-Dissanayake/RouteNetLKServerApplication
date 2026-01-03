package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterAssignmentDetailedResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterAssignmentMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignement;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RosterAssignmentService {

    private final RosterAssignmentRepository rosterAssignmentRepository;
    private final RosterAssignmentMapper rosterAssignmentMapper;

    public List<RosterAssignmentDetailedResponseDto> getRosterAssignments() {
        List<Rosterassignement> rosterAssignments =rosterAssignmentRepository.findAll();
        return rosterAssignmentMapper.toDetailList(rosterAssignments);
    }
}

