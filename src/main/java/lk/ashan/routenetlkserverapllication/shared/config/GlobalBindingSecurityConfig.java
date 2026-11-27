package lk.ashan.routenetlkserverapllication.shared.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Set;

/**
 * Security Hardening & Compensating Controls:
 * 1. Mitigates CVE-2026-59282 (Spring Data Binding DoS via deep/dangerous property paths).
 * 2. Mitigates CVE-2026-47834 (Spring Data JPA Sort parameter injection bypass).
 */
@Configuration
@ControllerAdvice
public class GlobalBindingSecurityConfig implements WebMvcConfigurer {

    private static final Set<String> ALLOWED_SORT_PROPERTIES = Set.of(
            "id", "createdAt", "updatedAt", "name", "title", "status", "email"
    );

    @InitBinder
    public void configureDataBinder(WebDataBinder binder) {
        binder.setDisallowedFields("class.*", "Class.*", "*.class.*", "*.Class.*");
        binder.setAutoGrowCollectionLimit(256);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        SortHandlerMethodArgumentResolver sortResolver = new SortHandlerMethodArgumentResolver() {
            @Override
            public @NotNull Sort resolveArgument(@NotNull MethodParameter parameter,
                                                 ModelAndViewContainer mavContainer,
                                                 @NotNull NativeWebRequest webRequest,
                                                 WebDataBinderFactory binderFactory) {
                Sort rawSort = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
                List<Sort.Order> safeOrders = rawSort.stream()
                        .filter(order -> isSafeSortProperty(order.getProperty()))
                        .toList();
                return safeOrders.isEmpty() ? Sort.unsorted() : Sort.by(safeOrders);
            }
        };

        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver(sortResolver);
        pageableResolver.setMaxPageSize(100);
        pageableResolver.setFallbackPageable(PageRequest.of(0, 20));
        resolvers.add(pageableResolver);
        resolvers.add(sortResolver);
    }

    private boolean isSafeSortProperty(String property) {
        if (property == null || property.length() > 50 || !property.matches("^[a-zA-Z0-9_]+$")) {
            return false;
        }
        return ALLOWED_SORT_PROPERTIES.contains(property);
    }
}
