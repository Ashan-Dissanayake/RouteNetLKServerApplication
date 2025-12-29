package lk.ashan.routenetlkserverapllication.module.crew.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@Entity
public class Driver {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "number")
    private String number;
    @Basic
    @Column(name = "licensenumber")
    private String licensenumber;
    @Basic
    @Column(name = "dolicenseissued")
    private LocalDate dolicenseissued;
    @Basic
    @Column(name = "dolicenseexpired")
    private LocalDate dolicenseexpired;
    @Basic
    @Column(name = "domedicalissued")
    private LocalDate domedicalissued;
    @Basic
    @Column(name = "domedicalexpired")
    private LocalDate domedicalexpired;
    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "licensecategory_id", referencedColumnName = "id", nullable = false)
    private Licensecategory licensecategory;
    @ManyToOne
    @JoinColumn(name = "crewstatus_id", referencedColumnName = "id", nullable = false)
    private Crewstatus crewstatus;
    @ManyToOne
    @JoinColumn(name = "routefamiliaritylevel_id", referencedColumnName = "id", nullable = false)
    private Routefamiliaritylevel routefamiliaritylevel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Objects.equals(id, driver.id) && Objects.equals(number, driver.number) && Objects.equals(licensenumber, driver.licensenumber) && Objects.equals(dolicenseexpired, driver.dolicenseexpired) && Objects.equals(domedicalexpired, driver.domedicalexpired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, licensenumber, dolicenseexpired, domedicalexpired);
    }

}
