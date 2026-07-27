package lk.ashan.routenetlkserverapllication.module.grn.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.shared.audit.BranchAnnotationListener;
import lk.ashan.routenetlkserverapllication.shared.audit.CurrentBranch;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@FilterDef(name = "branchAndUserFilter", parameters = {
//        @ParamDef(name = "branchId", type = Integer.class),
//        @ParamDef(name = "userId", type = Integer.class)
//})
@Filter(name = "branchFilter", condition = "branch_id = :branchId")
@EntityListeners({AuditingEntityListener.class, BranchAnnotationListener.class})
public class Grn {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "number")
    private String number;

    @Basic
    @Column(name = "doreceived")
    private Date doreceived;

    @Basic
    @Column(name = "remarks")
    private String remarks;

    @CurrentBranch
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "partrequest_id", referencedColumnName = "id", nullable = false)
    private PartRequest partrequest;

    @ManyToOne
    @JoinColumn(name = "grnstatus_id", referencedColumnName = "id", nullable = false)
    private GrnStatus grnstatus;

    @OneToMany(mappedBy = "grn", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<GrnPartRequestItem> grnpartrequestitems;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grn grn = (Grn) o;
        return Objects.equals(id, grn.id) && Objects.equals(number, grn.number) && Objects.equals(doreceived, grn.doreceived) && Objects.equals(remarks, grn.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, doreceived, remarks);
    }

}
