package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignmentStatus;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterShiftAssignmentStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RosterShiftAssignmentStatusService {

    private final RosterShiftAssignmentStatusRepository rosterShiftAssignmentStatusRepository;

    @Transactional(readOnly = true)
    public RosterShiftAssignmentStatus getById(Integer id) {
        return rosterShiftAssignmentStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

    @Transactional(readOnly = true)
    public RosterShiftAssignmentStatus getByName(String name) {
        return rosterShiftAssignmentStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

}
