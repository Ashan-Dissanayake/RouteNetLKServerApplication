package lk.ashan.routenetlkserverapllication.module.permit.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
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
public class Permite extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "number")
    private String number;
    @Basic
    @Column(name = "doissued")
    private LocalDate doissued;
    @Basic
    @Column(name = "doexpired")
    private LocalDate doexpired;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "permitestatus_id", referencedColumnName = "id", nullable = false)
    private PermiteStatus permitestatus;
    @ManyToOne
    @JoinColumn(name = "servicetype_id", referencedColumnName = "id", nullable = false)
    private ServiceType servicetype;
    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "id", nullable = false)
    private Route route;

    @OneToMany(mappedBy = "permite")
    private Collection<Trip> trips;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permite permite = (Permite) o;
        return Objects.equals(id, permite.id) && Objects.equals(number, permite.number) && Objects.equals(doissued, permite.doissued) && Objects.equals(doexpired, permite.doexpired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, doissued, doexpired);
    }

}
