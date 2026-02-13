package lk.ashan.routenetlkserverapllication.module.trip.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tripvehicleoverride {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "reason")
    private String reason;
    @Basic
    @Column(name = "dooverride")
    private LocalDate dooverride;
    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
    private Trip trip;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "overridestatus_id", referencedColumnName = "id", nullable = false)
    private Overridestatus overridestatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tripvehicleoverride that = (Tripvehicleoverride) o;
        return Objects.equals(id, that.id) && Objects.equals(reason, that.reason) && Objects.equals(dooverride, that.dooverride);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reason, dooverride);
    }
}
