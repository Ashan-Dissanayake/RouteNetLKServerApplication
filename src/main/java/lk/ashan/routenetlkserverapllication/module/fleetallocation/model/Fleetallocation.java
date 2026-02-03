package lk.ashan.routenetlkserverapllication.module.fleetallocation.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lombok.*;

import java.sql.Time;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Fleetallocation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "todepature")
    private Time todepature;
    @Basic
    @Column(name = "toreturn")
    private Time toreturn;
    @Basic
    @Column(name = "startmileage")
    private Integer startmileage;
    @Basic
    @Column(name = "endmileage")
    private Integer endmileage;
    @ManyToOne
    @JoinColumn(name = "roster_id", referencedColumnName = "id", nullable = false)
    private Roster roster;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "allocatedbranch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "fleetallocationstatus_id", referencedColumnName = "id", nullable = false)
    private Fleetallocationstatus fleetallocationstatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fleetallocation that = (Fleetallocation) o;
        return Objects.equals(id, that.id) && Objects.equals(todepature, that.todepature) && Objects.equals(toreturn, that.toreturn) && Objects.equals(startmileage, that.startmileage) && Objects.equals(endmileage, that.endmileage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, todepature, toreturn, startmileage, endmileage);
    }

}
