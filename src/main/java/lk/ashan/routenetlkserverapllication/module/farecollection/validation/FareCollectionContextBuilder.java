package lk.ashan.routenetlkserverapllication.module.farecollection.validation;

import lk.ashan.routenetlkserverapllication.module.farecollection.model.dto.FareCollectionCreateRequestDto;
import org.springframework.stereotype.Component;

/**
 * Builder class for creating instances of FareCollectionValidationContext.
 */
@Component
public class FareCollectionContextBuilder {

    /**
     * Builds a FareCollectionValidationContext object from the provided DTO.
     *
     * @param dto the FareCollectionCreateRequestDto containing the data to build the context
     * @return a FareCollectionValidationContext object populated with data from the DTO
     * @throws IllegalArgumentException if the provided DTO is null
     */
    public FareCollectionValidationContext build(FareCollectionCreateRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Request DTO cannot be null");
        }

        return FareCollectionValidationContext.builder()
                .branchId(dto.getBranch() != null ? dto.getBranch().getId() : null)
                .tripExecutionId(dto.getTripexecution() != null ? dto.getTripexecution().getId() : null)
                .ticketMachineId(dto.getTicketmachine() != null ? dto.getTicketmachine().getId() : null)
                .totalTickets(dto.getTotaltickets())
                .cashCollected(dto.getCachecollected())
                .digitalPayments(dto.getDigitalpayments())
                .build();
    }
}
