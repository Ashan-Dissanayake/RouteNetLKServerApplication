package lk.ashan.routenetlkserverapllication.module.roster.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.Tripcrewallocation;
import lombok.*;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shift {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "tostart")
    private LocalTime tostart;
    @Basic
    @Column(name = "toend")
    private LocalTime toend;
    @Basic
    @Column(name = "maxhours")
    private Integer maxhours;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "shiftstatus_id", referencedColumnName = "id", nullable = false)
    private Shiftstatus shiftstatus;
    @OneToMany(mappedBy = "shift")
    private Collection<Shiftrosterassignment> shiftrosterassignments;

    @OneToMany(mappedBy = "derivedshift")
    private Collection<Tripcrewallocation> tripcrewallocations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return Objects.equals(id, shift.id) && Objects.equals(name, shift.name) && Objects.equals(tostart, shift.tostart) && Objects.equals(toend, shift.toend) && Objects.equals(maxhours, shift.maxhours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tostart, toend, maxhours);
    }

}
