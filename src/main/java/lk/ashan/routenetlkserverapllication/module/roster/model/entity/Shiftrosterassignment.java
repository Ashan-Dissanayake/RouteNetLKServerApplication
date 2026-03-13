package lk.ashan.routenetlkserverapllication.module.roster.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shiftrosterassignment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "doassigned")
    private LocalDate doassigned;
    @ManyToOne
    @JoinColumn(name = "shift_id", referencedColumnName = "id", nullable = false)
    private Shift shift;
    @ManyToOne
    @JoinColumn(name = "roster_id", referencedColumnName = "id", nullable = false)
    private Roster roster;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "shiftrosterassignmentstatus_id", referencedColumnName = "id", nullable = false)
    private Shiftrosterassignmentstatus shiftrosterassignmentstatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shiftrosterassignment that = (Shiftrosterassignment) o;
        return Objects.equals(id, that.id) && Objects.equals(doassigned, that.doassigned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doassigned);
    }

}
