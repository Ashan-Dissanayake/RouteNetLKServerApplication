package lk.ashan.routenetlkserverapllication.shared.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

@Getter
@Setter
@ToString
@MappedSuperclass
@FilterDef(
        name = "softDeleteFilter",
        parameters = @ParamDef(name = "is_deleted", type = Boolean.class)
)
@Filters({
        @Filter(name = "softDeleteFilter", condition = "deleted = :is_deleted")
})
public abstract class BaseEntity {
    @Column(name = "deleted")
    private boolean deleted = false;
}

