package lk.ashan.routenetlkserverapllication.module.roster.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Roster {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "doroster")
    private LocalDate doroster;
    @ManyToOne
    @JoinColumn(name = "shift_id", referencedColumnName = "id", nullable = false)
    private Shift shift;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @OneToMany(mappedBy = "roster")
    private Collection<Rosterassignement> rosterassignements;
    @ManyToOne
    @JoinColumn(name = "rosterstatus_id", referencedColumnName = "id", nullable = false)
    private Rosterstatus rosterstatus;

    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    private Route route;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Roster roster = (Roster) o;
        return Objects.equals(id, roster.id) && Objects.equals(doroster, roster.doroster);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doroster);
    }

}
