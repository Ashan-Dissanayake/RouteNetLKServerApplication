package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "incidentvehicleallocation", schema = "routenetlk")
public class IncidentVehicleAllocation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "doassigned")
    private LocalDateTime doassigned;
    @Basic
    @Column(name = "doreleased")
    private LocalDateTime doreleased;
    @ManyToOne
    @JoinColumn(name = "incident_id", referencedColumnName = "id", nullable = false)
    private Incident incident;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "providedbranch_id", referencedColumnName = "id", nullable = false)
    private Branch providedbranch;

    @ManyToOne
    @JoinColumn(name = "incidentvehicleallocationstatus_id", referencedColumnName = "id", nullable = false)
    private IncidentVehicleAllocationStatus incidentvehicleallocationstatus;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncidentVehicleAllocation that = (IncidentVehicleAllocation) o;
        return Objects.equals(id, that.id) && Objects.equals(doassigned, that.doassigned) && Objects.equals(doreleased, that.doreleased);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doassigned, doreleased);
    }

}
