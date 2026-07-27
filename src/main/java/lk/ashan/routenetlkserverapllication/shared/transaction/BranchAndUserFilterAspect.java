package lk.ashan.routenetlkserverapllication.shared.transaction;

import jakarta.persistence.EntityManager;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.hibernate.Filter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class BranchAndUserFilterAspect {

    private final EntityManager entityManager;

    @Before("execution(* lk.ashan.routenetlkserverapllication.module.*.service..*.get*(..)) || " +
            "execution(* lk.ashan.routenetlkserverapllication.module.*.service..*.search*(..))")
    public void enableBranchAndUserFilter() {
        applyFilter();
    }

    @Around("@annotation(DisableUserFilter)")
    public Object manageFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        Session session = entityManager.unwrap(Session.class);
        boolean wasEnabled = session.getEnabledFilter("userFilter") != null;
        if (wasEnabled) {
            session.disableFilter("userFilter");
        }

        try {
            return joinPoint.proceed();
        } finally {
            if (wasEnabled) {
                applyFilter();
            }
        }
    }

    @Around("@annotation(DisableBranchFilter)")
    public Object manageBranchFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        Session session = entityManager.unwrap(Session.class);
        boolean wasEnabled = session.getEnabledFilter("branchFilter") != null;

        if (wasEnabled) {
            session.disableFilter("branchFilter");
        }

        try {
            return joinPoint.proceed();

        } finally {
            if (wasEnabled) {
                applyFilter();
            }
        }
    }

    private void applyFilter() {

        Session session = entityManager.unwrap(Session.class);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
                authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof CustomUserPrincipal principal) {

            if (!isAdmin(principal)) {

                User userEntity = principal.getUserEntity();

                Integer branchId =
                        (userEntity.getEmployee() != null &&
                                userEntity.getEmployee().getBranch() != null)
                                ? userEntity.getEmployee().getBranch().getId()
                                : null;

                Integer userId = userEntity.getId();


                // Existing branch + user filter
//                if (session.getEnabledFilter("branchAndUserFilter") == null) {
//
//                    session.enableFilter("branchAndUserFilter")
//                            .setParameter("branchId", branchId)
//                            .setParameter("userId", userId);
//                }


                // Add this for branchFilter
                if (session.getEnabledFilter("branchFilter") == null) {

                    session.enableFilter("branchFilter")
                            .setParameter("branchId", branchId);
                }


            } else {

                //session.disableFilter("branchAndUserFilter");
                session.disableFilter("branchFilter");
            }
        }
    }



    /**
     * Centralized admin check logic.
     */
    private boolean isAdmin(CustomUserPrincipal principal) {
        return principal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_SYSTEM_ADMIN") ||
                        auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
