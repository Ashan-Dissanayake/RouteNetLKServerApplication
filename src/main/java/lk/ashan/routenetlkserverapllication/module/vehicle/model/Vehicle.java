package lk.ashan.routenetlkserverapllication.module.vehicle.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.shared.model.BaseEntity;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Year;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vehicle extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "code")
    private String code;
    @Basic
    @Column(name = "number")
    private String number;
    @Basic
    @Column(name = "seatingcapacity")
    private Integer seatingcapacity;
    @Basic
    @Column(name = "yom")
    private Year yom;
    @Basic
    @Column(name = "dob")
    private LocalDate dob;
    @Basic
    @Column(name = "mileage")
    private Integer mileage;
    @Basic
    @Column(name = "chasisnumber")
    private String chasisnumber;
    @Basic
    @Column(name = "enginenumber")
    private String enginenumber;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @Basic
    @Column(name = "deleted")
    private Boolean deleted;
    @ManyToOne
    @JoinColumn(name = "make_id", referencedColumnName = "id", nullable = false)
    private Make make;
    @ManyToOne
    @JoinColumn(name = "fueltype_id", referencedColumnName = "id", nullable = false)
    private Fueltype fueltype;
    @ManyToOne
    @JoinColumn(name = "conditionrate_id", referencedColumnName = "id", nullable = false)
    private Conditionrate conditionrate;
    @ManyToOne
    @JoinColumn(name = "servicetype_id", referencedColumnName = "id", nullable = false)
    private Servicetype servicetype;
    @ManyToOne
    @JoinColumn(name = "vehiclestatus_id", referencedColumnName = "id", nullable = false)
    private Vehiclestatus vehiclestatus;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id) && Objects.equals(code, vehicle.code) && Objects.equals(number, vehicle.number) && Objects.equals(seatingcapacity, vehicle.seatingcapacity) && Objects.equals(yom, vehicle.yom) && Objects.equals(dob, vehicle.dob) && Objects.equals(mileage, vehicle.mileage) && Objects.equals(chasisnumber, vehicle.chasisnumber) && Objects.equals(enginenumber, vehicle.enginenumber) && Objects.equals(remarks, vehicle.remarks) && Objects.equals(deleted, vehicle.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, number, seatingcapacity, yom, dob, mileage, chasisnumber, enginenumber, remarks, deleted);
    }

}
