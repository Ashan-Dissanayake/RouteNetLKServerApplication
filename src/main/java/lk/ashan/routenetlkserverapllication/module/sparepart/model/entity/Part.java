package lk.ashan.routenetlkserverapllication.module.sparepart.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestItem;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServicePart;
import lk.ashan.routenetlkserverapllication.shared.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Filter(name = "branchFilter", condition = "branch_id = :branchId")
public class Part extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "qoh")
    private BigDecimal qoh;
    @Basic
    @Column(name = "maxlevel")
    private BigDecimal maxlevel;
    @Basic
    @Column(name = "rop")
    private BigDecimal rop;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @Basic
    @Column(name = "dolastordered")
    private LocalDate dolastordered;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "partstatus_id", referencedColumnName = "id", nullable = false)
    private Partstatus partstatus;
    @ManyToOne
    @JoinColumn(name = "partmaster_id", referencedColumnName = "id", nullable = false)
    private Partmaster partmaster;

    @OneToMany(mappedBy = "part")
    private Collection<PartRequestItem> partRequestItems;

    @OneToMany(mappedBy = "part")
    private Collection<VehicleServicePart> vehicleServiceParts;
    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return Objects.equals(id, part.id) && Objects.equals(qoh, part.qoh) && Objects.equals(maxlevel, part.maxlevel) && Objects.equals(rop, part.rop) && Objects.equals(dolastordered, part.dolastordered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, qoh, maxlevel, rop, dolastordered);
    }
}
