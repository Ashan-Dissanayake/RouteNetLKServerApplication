package lk.ashan.routenetlkserverapllication.module.permit.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.permit.mapper.PermitMapper;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitStatusRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.ServiceTypeRepository;
import lk.ashan.routenetlkserverapllication.module.permit.state.PermitState;
import lk.ashan.routenetlkserverapllication.module.permit.state.PermitStateFactory;
import lk.ashan.routenetlkserverapllication.module.permit.state.PermitStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.permit.validation.PermitValidationContext;
import lk.ashan.routenetlkserverapllication.module.permit.validation.PermitValidationContextBuilder;
import lk.ashan.routenetlkserverapllication.module.permit.validation.PermitValidationStrategy;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableBranchFilter;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermitService {

    private final PermitRepository permitRepository;
    private final VehicleRepository vehicleRepository;
    private final RouteRepository routeRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final PermitStatusRepository permitStatusRepository;
    private final PermitMapper permitMapper;
    private final PermitStateTransitionHandler permitStateTransitionHandler;

    private final List<PermitValidationStrategy> validationStrategies;
    private final PermitStateFactory permitStateFactory;
    private final PermitValidationContextBuilder permitValidationContextBuilder;


    @Transactional(readOnly = true)
    public List<PermitDetailResponseDto> getPermits(){
        return permitMapper.toDtoList(permitRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<PermitDetailResponseDto> searchPermit(
            @NotNull HashMap<String, String> params) {

        Specification<Permite> specification = (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            String number = params.get("ssnumber");
            String permitStatusId = params.get("sspermitstatus");
            String routeId = params.get("ssroute");

            if (number != null && !number.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("number")),
                                number.toLowerCase()
                        )
                );
            }

            if (permitStatusId != null && !permitStatusId.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("permitestatus").get("id"),
                                Integer.parseInt(permitStatusId)
                        )
                );
            }

            if (routeId != null && !routeId.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("route").get("id"),
                                Integer.parseInt(routeId)
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };

        List<Permite> permits =
                permitRepository.findAll(specification);

        return permitMapper.toDtoList(permits);
    }


    @Transactional(readOnly = true)
    public List<PermitSummaryResponseDto> getSummaryPermits() {
        return permitMapper.toSummaryDtoList(permitRepository.findAll());
    }

    @Transactional
    @DisableSoftDeleteFilter
    @DisableBranchFilter
    public PermitDetailResponseDto createPermit(@NotNull PermitCreateRequestDto requestDto) {

        if (permitRepository.existsByNumber(requestDto.getNumber())) {
            throw new ResourceExistsException("Permit number already exists.");
        }

        vehicleRepository.findById(requestDto.getVehicle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        routeRepository.findById(requestDto.getRoute().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));

         serviceTypeRepository
                .findById(requestDto.getServicetype().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Service type not found"));

        PermitValidationContext context = permitValidationContextBuilder.buildForCreate(requestDto);

        validationStrategies.forEach(strategy -> strategy.validate(context));

        Permite permite = permitMapper.toEntity(requestDto);

        PermiteStatus requestedStatus = permitStatusRepository
                .findByName(requestDto.getPermitestatus().getName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Permit status not found: " + requestDto.getPermitestatus().getName()));

        PermitState state = permitStateFactory.getState(requestedStatus.getName());
        state.validateInitial();

        Permite saved = permitRepository.save(permite);
        return permitMapper.toDto(saved);
    }

    @Transactional
    public PermitDetailResponseDto transferPermit(Integer permitId) {

        Permite permite = permitRepository.findById(permitId)
                .orElseThrow(() -> new ResourceNotFoundException("Permit not found"));

        PermiteStatus currentStatus = permite.getPermitestatus();

        if ("Transferred".equalsIgnoreCase(currentStatus.getName())) {
            throw new BusinessRuleViolationException("Permit is already transferred");}

        PermiteStatus newStatus = permitStatusRepository.findByName("Transferred")
                        .orElseThrow(() -> new ResourceNotFoundException("Target permit status not found"));

        permitStateTransitionHandler.transitionTo(permite, newStatus);

        Permite savedPermite = permitRepository.save(permite);

        return permitMapper.toDto(savedPermite);
    }
}
