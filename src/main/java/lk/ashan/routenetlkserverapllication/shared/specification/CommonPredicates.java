package lk.ashan.routenetlkserverapllication.shared.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


public final class CommonPredicates {

    private CommonPredicates() {
    }

    public static <T> Predicate hasId(
            Root<T> root,
            CriteriaBuilder criteriaBuilder,
            String field,
            Integer id) {

        return criteriaBuilder.equal(
                root.get(field).get("id"),
                id
        );
    }
}