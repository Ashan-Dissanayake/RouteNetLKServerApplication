package lk.ashan.routenetlkserverapllication.shared.audit;

import jakarta.persistence.PrePersist;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.security.CustomUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.lang.reflect.Field;

public class BranchAnnotationListener {

    @PrePersist
    public void assignBranch(Object entity) {
        Class<?> clazz = entity.getClass();

        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(CurrentBranch.class)) {
                    field.setAccessible(true);
                    try {
                        Object currentValue = field.get(entity);
                        if (currentValue == null) {
                            Branch userBranch = getLoggedInUserBranch();
                            if (userBranch != null) {
                                field.set(entity, userBranch);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private Branch getLoggedInUserBranch() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof CustomUserPrincipal principal) {
            User loggedUser = principal.getUserEntity();
            if (loggedUser != null) {
                return loggedUser.getEmployee().getBranch();
            }
        }
        return null;
    }
}
