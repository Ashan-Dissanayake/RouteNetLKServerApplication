package lk.ashan.routenetlkserverapllication.shared.transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SoftDeleteFilterAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Before("execution(* lk.ashan.routenetlkserverapllication.module.*.service..*.get*(..)) || " +
            "execution(* lk.ashan.routenetlkserverapllication.module.*.service..*.search*(..))"
    )
    public void enableSoftDeleteFilter() {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.getEnabledFilter("softDeleteFilter");
        if (filter == null) {
            session.enableFilter("softDeleteFilter").setParameter("is_deleted", false);
        }
    }

    @Around("@annotation(DisableSoftDeleteFilter)")
    public Object manageFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.getEnabledFilter("softDeleteFilter");

        // Disable filter temporarily
        if (filter != null) {
            session.disableFilter("softDeleteFilter");
        }
        try {
            return joinPoint.proceed();
        } finally {
            // Re-enable after execution
            session.enableFilter("softDeleteFilter").setParameter("is_deleted", false);
        }
    }
}

