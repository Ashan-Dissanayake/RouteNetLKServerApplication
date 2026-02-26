package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.roster.model.Role;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shift;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lombok.*;

import java.time.LocalTime;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tripcrewallocation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @Basic
    @Column(name = "toallocated")
    private LocalTime toallocated;
    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
    private Trip trip;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "derivedshift_id", referencedColumnName = "id", nullable = false)
    private Shift derivedshift;
    @ManyToOne
    @JoinColumn(name = "tripallocationstatus_id", referencedColumnName = "id", nullable = false)
    private Tripallocationstatus tripallocationstatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tripcrewallocation that = (Tripcrewallocation) o;
        return Objects.equals(id, that.id) && Objects.equals(remarks, that.remarks);
    }

}
