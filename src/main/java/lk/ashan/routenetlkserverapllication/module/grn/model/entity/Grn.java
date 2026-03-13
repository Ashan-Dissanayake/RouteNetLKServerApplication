package lk.ashan.routenetlkserverapllication.module.grn.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.Partrequest;
import lombok.*;

import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "partrequest_id", referencedColumnName = "id", nullable = false)
    private Partrequest partrequest;
    @ManyToOne
    @JoinColumn(name = "grnstatus_id", referencedColumnName = "id", nullable = false)
    private Grnstatus grnstatus;
    @OneToMany(mappedBy = "grn")
    private Collection<Grnpart> grnparts;

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
