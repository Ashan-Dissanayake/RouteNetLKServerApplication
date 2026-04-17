package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.service;

//@Service
//@RequiredArgsConstructor
//public class TripCrewAllocationService {
//
//    private final TripCrewAllocationRepository tripCrewAllocationRepository;
//    private final TripRepository tripRepository;
////    private final ShiftRosterAssignmentRepository shiftRosterAssignmentRepository;
//    private final EmployeeRepository employeeRepository;
//
//    private final TripCrewAllocationMapper tripCrewAllocationMapper;
//
//    private final ShiftDeterminationService shiftDeterminationService;
//    private final RoleRepository roleRepository;
//    private final TripCrewAllocationSolverService solverService;
//    private final TripAllocationStatusRepository tripAllocationStatusRepository;
//    private final TripCrewAllocationStateFactory tripCrewAllocationStateFactory;
//
//
//    @Transactional(readOnly = true)
//    public List<TripCrewAllocationDetailResponseDto> getTripAllocations() {
//        return tripCrewAllocationMapper.toDtoList(tripCrewAllocationRepository.findAll());
//    }
//
//    @Transactional(readOnly = true)
//    public List<TripCrewAllocationDetailResponseDto> searchTripCrewAllocations(@NotNull HashMap<String, String> params) {
//
//        List<Tripcrewallocation> tripCrewAllocations = tripCrewAllocationRepository.findAll();
//
//        if (!params.isEmpty()) {
//
//            String tripId = params.get("sstripid");
//            String tripAllocationStatusId = params.get("sstripallocationstatusid");
//
//            Stream<Tripcrewallocation> tripcrewallocationStream = tripCrewAllocations.stream();
//
//            if (tripId != null)
//                tripcrewallocationStream = tripcrewallocationStream.filter(t -> t.getTrip().getId() == Integer.parseInt(tripId));
//            if (tripAllocationStatusId != null)
//                tripcrewallocationStream = tripcrewallocationStream.filter(t -> t.getTripallocationstatus().getId() == Integer.parseInt(tripAllocationStatusId));
//
//            return tripCrewAllocationMapper.toDtoList(tripcrewallocationStream.collect(Collectors.toList()));
//        }
//
//        return tripCrewAllocationMapper.toDtoList(tripCrewAllocations);
//    }
//
//
//    @Transactional
//    public TripCrewAllocationSuggestionResponseDto generateSuggestions(Integer tripId) {
//        Trip trip = tripRepository.findById(tripId)
//                .orElseThrow(() -> new ResourceNotFoundException("Trip not found: " + tripId));
//
//        Shift derivedShift = shiftDeterminationService.determineShiftForTrip(
//                trip.getTodepature(),
//                trip.getBranch().getId()
//        );
//
//        List<Rostershiftassignment> confirmedRoster = shiftRosterAssignmentRepository
//                .findByShift_IdAndDoassignedAndShiftrosterassignmentstatus_Name(
//                        derivedShift.getId(),
//                        trip.getDoservice(),
//                        "Confirmed"
//                );
//
//        if (confirmedRoster.isEmpty()) {
//            throw new BusinessRuleViolationException(
//                    "No confirmed roster assignments found for shift " + derivedShift.getName() +
//                            " on date " + trip.getDoservice()
//            );
//        }
//
//        List<Integer> employeeIds = confirmedRoster.stream()
//                .map(ra -> ra.getEmployee().getId())
//                .distinct()
//                .collect(Collectors.toList());
//
//        List<Employee> candidates = employeeRepository.findByIdInWithCrewData(employeeIds);
//
//        Role driverRole = roleRepository.findByName("Driver")
//                .orElseThrow(() -> new ResourceNotFoundException("Driver role not found"));
//        Role conductorRole = roleRepository.findByName("Conductor")
//                .orElseThrow(() -> new ResourceNotFoundException("Conductor role not found"));
//        List<Role> roles = List.of(driverRole, conductorRole);
//
//        List<Tripcrewallocation> existingAllocations =
//                tripCrewAllocationRepository.findByTrip_DoserviceAndTripallocationstatus_NameIn(
//                        trip.getDoservice(),
//                        List.of("Suggested", "Confirmed")
//                );
//
//        TripCrewScheduleSolution solution = solverService.solve(
//                trip,
//                derivedShift,
//                roles,
//                candidates,
//                confirmedRoster,
//                existingAllocations
//        );
//
//        if (!solution.isFeasible()) {
//            String report = solverService.getConstraintViolationReport(solution);
//            throw new BusinessRuleViolationException(
//                    "Could not generate feasible crew allocation. Score: " + solution.getScore()
//            );
//        }
//
//        List<TripCrewAllocationDetailResponseDto> savedAllocations =
//                saveSuggestions(solution, trip, derivedShift);
//
//        return TripCrewAllocationSuggestionResponseDto.builder()
//                .tripId(trip.getId())
//                .doservice(trip.getDoservice())
//                .todepature(trip.getTodepature())
//                .feasible(solution.isFeasible())
//                .score(solution.getScore().toString())
//                .suggestedAllocations(savedAllocations)
//                .assignmentsFilled((int) solution.getFilledCount())
//                .assignmentsUnfilled((int) solution.getUnfilledCount())
//                .build();
//    }
//
//    private List<TripCrewAllocationDetailResponseDto> saveSuggestions(
//            TripCrewScheduleSolution solution,
//            Trip trip,
//            Shift shift) {
//
//        Tripallocationstatus suggestedStatus = tripAllocationStatusRepository.findByName("Suggested")
//                .orElseThrow(() -> new ResourceNotFoundException("Status 'Suggested' not found"));
//
//        return solution.getFilledAssignments().stream()
//                .map(assignment -> {
//                    Employee employee = employeeRepository
//                            .findById(assignment.getAssignedEmployeeId())
//                            .orElseThrow(() -> new ResourceNotFoundException(
//                                    "Employee not found: " + assignment.getAssignedEmployeeId()));
//                    Tripcrewallocation allocation = Tripcrewallocation.builder()
//                            .trip(trip)
//                            .employee(employee)
//                            .role(assignment.getRole())
//                            .derivedshift(shift)
//                            .tripallocationstatus(suggestedStatus)
//                            .toallocated(LocalTime.now())
//                            .build();
//
//                    Tripcrewallocation saved = tripCrewAllocationRepository.save(allocation);
//
//                    return tripCrewAllocationMapper.toDto(saved);
//                })
//                .collect(Collectors.toList());
//    }
//
//
//    @Transactional
//    public TripCrewAllocationDetailResponseDto approveSuggestion(Integer allocationId) {
//
//        Tripcrewallocation allocation = tripCrewAllocationRepository.findById(allocationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found: " + allocationId));
//
//        // Get current state
//        TripCrewAllocationState currentState = tripCrewAllocationStateFactory.getState(
//                allocation.getTripallocationstatus().getName()
//        );
//
//        // Get target status
//        Tripallocationstatus confirmedStatus = tripAllocationStatusRepository.findByName("Confirmed")
//                .orElseThrow(() -> new ResourceNotFoundException("Status 'Confirmed' not found"));
//
//        // Validate transition using State Pattern
//        currentState.transitionTo(allocation, confirmedStatus);
//
//        // Save
//        Tripcrewallocation saved = tripCrewAllocationRepository.save(allocation);
//
//        return tripCrewAllocationMapper.toDto(saved);
//    }
//
//
//    @Transactional
//    public TripCrewAllocationDetailResponseDto rejectSuggestion(Integer allocationId) {
//
//        Tripcrewallocation allocation = tripCrewAllocationRepository.findById(allocationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found: " + allocationId));
//
//        // Get current state
//        TripCrewAllocationState currentState = tripCrewAllocationStateFactory.getState(
//                allocation.getTripallocationstatus().getName()
//        );
//
//        // Get target status
//        Tripallocationstatus rejectedStatus = tripAllocationStatusRepository.findByName("Rejected")
//                .orElseThrow(() -> new ResourceNotFoundException("Status 'Rejected' not found"));
//
//        // Validate transition using State Pattern
//        currentState.transitionTo(allocation, rejectedStatus);
//
//        // Save
//        Tripcrewallocation saved = tripCrewAllocationRepository.save(allocation);
//
//        return tripCrewAllocationMapper.toDto(saved);
//    }
//
//
//    @Transactional
//    public void clearRejectedAllocations(Integer tripId) {
//        List<Tripcrewallocation> rejectedAllocations = tripCrewAllocationRepository
//                .findByTrip_DoserviceAndTripallocationstatus_NameIn(
//                        tripRepository.findById(tripId)
//                                .orElseThrow(() -> new ResourceNotFoundException("Trip not found: " + tripId))
//                                .getDoservice(),
//                        List.of("Rejected")
//                );
//
//        tripCrewAllocationRepository.deleteAll(rejectedAllocations);
//    }
//
//}
