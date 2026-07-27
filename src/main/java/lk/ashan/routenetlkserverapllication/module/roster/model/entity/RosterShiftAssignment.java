package lk.ashan.routenetlkserverapllication.module.roster.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lombok.*;

import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rostershiftassignment", schema = "routenetlk")
public class RosterShiftAssignment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "rostershift_id", referencedColumnName = "id", nullable = false)
    private RosterShift rostershift;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "rostershiftassignmentstatus_id", referencedColumnName = "id", nullable = false)
    private RosterShiftAssignmentStatus rostershiftassignmentstatus;

    public Integer getEffectiveFamiliarity() {
        if (employee == null) return 0;

        // Check Driver role
        if (employee.getDriver() != null && employee.getDriver().getRoutefamiliaritylevel() != null) {
            return employee.getDriver().getRoutefamiliaritylevel().getId();
        }

        // Check Conductor role
        if (employee.getConductor() != null && employee.getConductor().getRoutefamiliaritylevel() != null) {
            return employee.getConductor().getRoutefamiliaritylevel().getId();
        }

        return 1; // Default to 'Low' (1) rather than 0 to match your logic
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RosterShiftAssignment that = (RosterShiftAssignment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
