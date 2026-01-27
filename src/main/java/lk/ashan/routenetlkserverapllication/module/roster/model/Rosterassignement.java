package lk.ashan.routenetlkserverapllication.module.roster.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lombok.*;


import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rosterassignement {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "roster_id", referencedColumnName = "id", nullable = false)
    private Roster roster;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rosterassignement that = (Rosterassignement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
