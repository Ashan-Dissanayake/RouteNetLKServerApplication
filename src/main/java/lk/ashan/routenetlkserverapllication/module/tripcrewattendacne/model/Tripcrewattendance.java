package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.roster.model.Role;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lombok.*;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tripcrewattendance {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "tocheckin")
    private LocalTime tocheckin;
    @Basic
    @Column(name = "tocheckout")
    private LocalTime tocheckout;
    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
    private Trip trip;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "plannedemployee_id", referencedColumnName = "id", nullable = false)
    private Employee plannedemployee;
    @ManyToOne
    @JoinColumn(name = "actualemployee_id", referencedColumnName = "id", nullable = true)
    private Employee actualemployee;
    @ManyToOne
    @JoinColumn(name = "crewattendancestatus_id", referencedColumnName = "id", nullable = false)
    private Crewattendancestatus crewattendancestatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tripcrewattendance that = (Tripcrewattendance) o;
        return Objects.equals(id, that.id) && Objects.equals(tocheckin, that.tocheckin) && Objects.equals(tocheckout, that.tocheckout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tocheckin, tocheckout);
    }

}
