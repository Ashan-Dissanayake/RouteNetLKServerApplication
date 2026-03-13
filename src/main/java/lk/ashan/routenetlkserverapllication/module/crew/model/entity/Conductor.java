package lk.ashan.routenetlkserverapllication.module.crew.model.entity;

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
public class Conductor {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "number")
    private String number;
    @Basic
    @Column(name = "domedicalissued")
    private LocalDate domedicalissued;
    @Basic
    @Column(name = "domedicalexpired")
    private LocalDate domedicalexpired;
    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "routefamiliaritylevel_id", referencedColumnName = "id", nullable = false)
    private Routefamiliaritylevel routefamiliaritylevel;
    @ManyToOne
    @JoinColumn(name = "crewstatus_id", referencedColumnName = "id", nullable = false)
    private Crewstatus crewstatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conductor conductor = (Conductor) o;
        return Objects.equals(id, conductor.id) && Objects.equals(number, conductor.number) && Objects.equals(domedicalissued, conductor.domedicalissued) && Objects.equals(domedicalexpired, conductor.domedicalexpired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, domedicalissued, domedicalexpired);
    }

}
