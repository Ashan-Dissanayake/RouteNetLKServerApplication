package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model;

import jakarta.persistence.*;
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
public class Vehicleserviceschedule {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "doscheduledstart")
    private LocalDate doscheduledstart;
    @Basic
    @Column(name = "doscheduledend")
    private LocalDate doscheduledend;
    @ManyToOne
    @JoinColumn(name = "vehicleservice_id", referencedColumnName = "id", nullable = false)
    private Vehicleservice vehicleservice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicleserviceschedule that = (Vehicleserviceschedule) o;
        return Objects.equals(id, that.id) && Objects.equals(doscheduledstart, that.doscheduledstart) && Objects.equals(doscheduledend, that.doscheduledend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doscheduledstart, doscheduledend);
    }
}
