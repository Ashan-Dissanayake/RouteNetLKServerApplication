package lk.ashan.routenetlkserverapllication.module.permit.schedule;

import jakarta.transaction.Transactional;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitStatusRepository;
import lk.ashan.routenetlkserverapllication.module.permit.state.PermitState;
import lk.ashan.routenetlkserverapllication.module.permit.state.PermitStateFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PermitExpiryScheduler {

    private final PermitRepository permitRepository;
    private final PermitStatusRepository permitStatusRepository;
    private final PermitStateFactory permitStateFactory;

    private static final String ACTIVE = "Active";
    private static final String EXPIRED = "Expired";

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void handlePermitExpiry() {

        LocalDate today = LocalDate.now();

        //Load target EXPIRED status once
        PermiteStatus expiredStatus = permitStatusRepository.findByName(EXPIRED)
                .orElseThrow(() -> new IllegalStateException("EXPIRED status not found"));

        //Find ACTIVE permits already expired by date
        List<Permite> permitsToExpire =
                permitRepository.findByPermitestatus_NameAndDoexpiredBefore(
                        ACTIVE, today
                );

        for (Permite permite : permitsToExpire) {

            //Resolve current state dynamically
            String currentStatus = permite.getPermitestatus().getName();
            PermitState state = permitStateFactory.getState(currentStatus);

            //Delegate transition to state machine
            state.transitionTo(permite, expiredStatus);
        }

        //Persist state changes
        permitRepository.saveAll(permitsToExpire);

        //Upcoming expiries (NO transition)
        LocalDate warningDate = today.plusDays(30);

        List<Permite> expiringSoon =
                permitRepository.findByPermitestatus_NameAndDoexpiredBetween(
                        ACTIVE, today, warningDate
                );

        logExpiringSoon(expiringSoon);
    }

    private void logExpiringSoon(List<Permite> permites) {
        // future: notification / event / audit
    }
}

