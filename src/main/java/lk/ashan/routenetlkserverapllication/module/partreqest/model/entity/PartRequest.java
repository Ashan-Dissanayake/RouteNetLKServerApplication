package lk.ashan.routenetlkserverapllication.module.partreqest.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.shared.audit.BranchAnnotationListener;
import lk.ashan.routenetlkserverapllication.shared.audit.CurrentBranch;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "partrequest", schema = "routenetlk")
//@FilterDef(name = "branchAndUserFilter", parameters = {
//        @ParamDef(name = "branchId", type = Integer.class),
//        @ParamDef(name = "userId", type = Integer.class)
//})
@Filter(name = "branchFilter", condition = "branch_id = :branchId")
@EntityListeners({AuditingEntityListener.class, BranchAnnotationListener.class})
public class PartRequest {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "number")
    private String number;
    @Basic
    @Column(name = "dorequested")
    private LocalDate dorequested;
    @Basic
    @Column(name = "remarks")
    private String remarks;

    @CurrentBranch
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "partrequeststatus_id", referencedColumnName = "id", nullable = false)
    private PartRequestStatus partrequeststatus;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User user;


    @OneToMany(
            mappedBy = "partrequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Collection<PartRequestItem> partrequestitems;

    @OneToMany(mappedBy = "partrequest")
    private Collection<Grn> grns;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartRequest that = (PartRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number) && Objects.equals(dorequested, that.dorequested) && Objects.equals(remarks, that.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, dorequested, remarks);
    }

}
