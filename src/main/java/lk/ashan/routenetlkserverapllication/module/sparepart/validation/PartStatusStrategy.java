package lk.ashan.routenetlkserverapllication.module.sparepart.validation;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.Partstatus;
import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PartStatusStrategy {

    private final PartStatusRepository partstatusRepository;

    public Partstatus determineStatus(PartCreationContext context) {

        if (context.getQoh().compareTo(BigDecimal.ZERO) == 0) {
            return getStatus("Out of stock");
        }

        if (context.getQoh().compareTo(context.getRop()) <= 0) {
            return getStatus("Low Stock");
        }

        return getStatus("Available");
    }

    private Partstatus getStatus(String name) {
        return partstatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Part status not found: " + name));
    }
}
