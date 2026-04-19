package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.service;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocationStatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.repository.TripCrewAllocationStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TripCrewAllocationStatusService {

    private final TripCrewAllocationStatusRepository tripCrewAllocationStatusRepository;

    @Transactional(readOnly = true)
    public TripCrewAllocationStatus getById(Integer id) {
        return tripCrewAllocationStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

    @Transactional(readOnly = true)
    public TripCrewAllocationStatus getByName(String name) {
        return tripCrewAllocationStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

}
