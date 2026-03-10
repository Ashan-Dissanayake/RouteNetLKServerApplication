package lk.ashan.routenetlkserverapllication.module.serviceshcedule.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.incident.model.Incident;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
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
public class Vehicleservice {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "dosuggestedstart")
    private Date dosuggestedstart;
    @Basic
    @Column(name = "dosuggestedend")
    private Date dosuggestedend;
    @Basic
    @Column(name = "docreated")
    private Date docreated;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "vehicleservicetype_id", referencedColumnName = "id", nullable = false)
    private Vehicleservicetype vehicleservicetype;
    @ManyToOne
    @JoinColumn(name = "incident_id", referencedColumnName = "id")
    private Incident incident;
    @ManyToOne
    @JoinColumn(name = "vehicleservicepriority_id", referencedColumnName = "id", nullable = false)
    private Vehicleservicepriority vehicleservicepriority;

    @OneToMany(mappedBy = "vehicleservice")
    private Collection<Vehicleservicepart> vehicleserviceparts;

    @ManyToOne
    @JoinColumn(name = "vehicleservicestatus_id", referencedColumnName = "id", nullable = false)
    private Vehicleservicestatus vehicleservicestatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicleservice that = (Vehicleservice) o;
        return Objects.equals(id, that.id) && Objects.equals(dosuggestedstart, that.dosuggestedstart) && Objects.equals(dosuggestedend, that.dosuggestedend) && Objects.equals(docreated, that.docreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dosuggestedstart, dosuggestedend, docreated);
    }
}
