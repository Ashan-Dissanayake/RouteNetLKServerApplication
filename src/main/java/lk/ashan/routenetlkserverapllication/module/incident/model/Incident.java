package lk.ashan.routenetlkserverapllication.module.incident.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.Incidentvehicleallocation;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lombok.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Incident {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "toreported")
    private LocalTime toreported;
    @Basic
    @Column(name = "doreported")
    private LocalDate doreported;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "incidenttype_id", referencedColumnName = "id", nullable = false)
    private Incidenttype incidenttype;
    @ManyToOne
    @JoinColumn(name = "incidentstatus_id", referencedColumnName = "id", nullable = false)
    private Incidentstatus incidentstatus;
    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
    private Trip trip;

    @OneToMany(mappedBy = "incident")
    private Collection<Incidentvehicleallocation> incidentvehicleallocations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incident incident = (Incident) o;
        return Objects.equals(id, incident.id) && Objects.equals(toreported, incident.toreported) && Objects.equals(doreported, incident.doreported) && Objects.equals(remarks, incident.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, toreported, doreported, remarks);
    }

}
