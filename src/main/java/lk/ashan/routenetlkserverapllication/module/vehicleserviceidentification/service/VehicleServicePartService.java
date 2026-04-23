package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.service;

import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.mapper.VehicleServicePartMapper;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleServicePart;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServicePartRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServicePartService {

    private final VehicleServicePartRepository vehicleServicePartRepository;
    private final VehicleServiceRepository vehicleServiceRepository;
    private final PartRepository partRepository;

    private final VehicleServicePartMapper vehicleServicePartMapper;

    @Transactional
    public List<VehicleServicePartDetailResponseDto> createParts(
            @NotNull VehicleServicePartBulkCreateRequestDto dto) {

        List<VehicleServicePartDetailResponseDto> results = new ArrayList<>();

        for (VehicleServicePartCreateRequestDto partDto : dto.getParts()) {
            VehicleService vehicleservice = getVehicleServiceOrThrow(partDto.getVehicleservice().getId());
            validateVehicleServiceStatus(vehicleservice);
            validatePartExists(partDto.getPart().getId());
            validateQuantity(partDto.getQuantity());

            VehicleServicePart entity = vehicleServicePartMapper.toEntity(partDto);
            VehicleServicePart saved = vehicleServicePartRepository.save(entity);
            results.add(vehicleServicePartMapper.toDto(saved));
        }

        return results;
    }

    @Transactional
    public List<VehicleServicePartDetailResponseDto> updateParts(
            @NotNull VehicleServicePartBulkUpdateRequestDto dto) {

        List<VehicleServicePartDetailResponseDto> results = new ArrayList<>();

        for (VehicleServicePartUpdateRequestDto partDto : dto.getParts()) {
            if (partDto.getId() == null) {
                throw new BusinessRuleViolationException("VehicleServicePart id is required for update");
            }

            VehicleServicePart existing = vehicleServicePartRepository
                    .findById(partDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "VehicleServicePart not found with id " + partDto.getId()
                    ));

            VehicleService vehicleservice = existing.getVehicleservice();
            validateVehicleServiceStatus(vehicleservice);
            validatePartExists(partDto.getPart().getId());
            validateQuantity(partDto.getQuantity());

            // Update existing entity in-place
            vehicleServicePartMapper.updateEntityFromDto(partDto, existing);

            VehicleServicePart saved = vehicleServicePartRepository.save(existing);
            results.add(vehicleServicePartMapper.toDto(saved));
        }

        return results;
    }

    // ---------------- HELPER METHODS ----------------
    private VehicleService getVehicleServiceOrThrow(Integer id) {
        return vehicleServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VehicleService not found with id " + id));
    }

    private void validateVehicleServiceStatus(VehicleService vehicleservice) {
        String statusName = vehicleservice.getVehicleservicestatus().getName().toUpperCase();
        if (!"CREATED".equals(statusName)) {
            throw new BusinessRuleViolationException("VehicleService must be in CREATED status to modify parts");
        }
    }

    private void validatePartExists(Integer partId) {
        partRepository.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found with id " + partId));
    }

    private void validateQuantity(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleViolationException("Quantity must be greater than zero");
        }
    }
}
