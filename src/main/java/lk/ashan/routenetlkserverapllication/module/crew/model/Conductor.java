package lk.ashan.routenetlkserverapllication.module.crew.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lombok.*;

import java.sql.Date;
import java.util.Objects;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Conductor {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "number")
    private String number;
    @Basic
    @Column(name = "domedicalexpired")
    private Date domedicalexpired;
    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id",unique = true)
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
        return Objects.equals(id, conductor.id) && Objects.equals(number, conductor.number) && Objects.equals(domedicalexpired, conductor.domedicalexpired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, domedicalexpired);
    }

}
