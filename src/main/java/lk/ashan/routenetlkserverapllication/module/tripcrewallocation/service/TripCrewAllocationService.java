package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.service;

import lk.ashan.routenetlkserverapllication.module.roster.event.RosterConfirmedEvent;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterShiftAssignmentRepository;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocation;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocationStatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.repository.TripCrewAllocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripCrewAllocationService {

    private final TripRepository tripRepository;
    private final RosterShiftAssignmentRepository rosterShiftAssignmentRepository;
    private final TripCrewAllocationRepository tripCrewAllocationRepository;
    private final TripCrewAllocationStatusService tripCrewAllocationStatusService;
}
