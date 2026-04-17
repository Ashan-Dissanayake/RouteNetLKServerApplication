package lk.ashan.routenetlkserverapllication.module.roster.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.shared.model.BaseEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Roster  extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "dostartofweek")
    private LocalDate dostartofweek;
    @Basic
    @Column(name = "doendofweek")
    private LocalDate doendofweek;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @OneToMany(mappedBy = "roster")
    private Collection<RosterShift> rosterShifts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Roster roster = (Roster) o;
        return Objects.equals(id, roster.id) && Objects.equals(dostartofweek, roster.dostartofweek) && Objects.equals(doendofweek, roster.doendofweek);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dostartofweek, doendofweek);
    }

}
