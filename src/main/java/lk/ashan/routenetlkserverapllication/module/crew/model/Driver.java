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
public class Driver{
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
    @Column(name = "dolicenseexpired")
    private Date dolicenseexpired;
    @Basic
    @Column(name = "domedicalexpired")
    private Date domedicalexpired;
    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id",unique = true)
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
    @ManyToOne
    @JoinColumn(name = "allowedbustype_id", referencedColumnName = "id", nullable = false)
    private Allowedbustype allowedbustype;

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
