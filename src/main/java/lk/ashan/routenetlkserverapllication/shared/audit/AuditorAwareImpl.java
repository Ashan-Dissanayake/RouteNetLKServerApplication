package lk.ashan.routenetlkserverapllication.shared.audit;

import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserRepository;
import lk.ashan.routenetlkserverapllication.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof CustomUserPrincipal principal) {

            // Logged user ge entity eka return karai
            return Optional.ofNullable(principal.getUserEntity());
        }

        // Authentication nethnam witharak fallback user kenek (Optional)
        // Nethnam meka ain karala Optional.empty() denna puluwan
        return Optional.empty();
    }
}
