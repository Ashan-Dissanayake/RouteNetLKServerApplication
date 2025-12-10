package lk.ashan.routenetlkserverapllication.module.employee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "number")
    private String number;
    @Basic
    @Column(name = "fullname")
    private String fullname;
    @Basic
    @Column(name = "callingname")
    private String callingname;
    @Basic
    @Column(name = "nic")
    private String nic;
    @Basic
    @Column(name = "mobile")
    private String mobile;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "emergencycontact")
    private String emergencycontact;
    @Basic
    @Column(name = "image")
    private byte[] image;
    @Basic
    @Column(name = "doj")
    private LocalDate doj;
    @ManyToOne
    @JoinColumn(name = "gender_id", referencedColumnName = "id", nullable = false)
    private Gender gender;
    @ManyToOne
    @JoinColumn(name = "branch_id",referencedColumnName = "id",nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = false)
    private Department department;
    @ManyToOne
    @JoinColumn(name = "designation_id", referencedColumnName = "id", nullable = false)
    private Designation designation;
    @ManyToOne
    @JoinColumn(name = "employeetype_id", referencedColumnName = "id", nullable = false)
    private Employeetype employeetype;
    @ManyToOne
    @JoinColumn(name = "employeestatus_id", referencedColumnName = "id", nullable = false)
    private Employeestatus employeestatus;
    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private Collection<Vehicle> vehicles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(
                id, employee.id) &&
                Objects.equals(number, employee.number) &&
                Objects.equals(fullname, employee.fullname) &&
                Objects.equals(nic, employee.nic) &&
                Objects.equals(mobile, employee.mobile) &&
                Objects.equals(email, employee.email) &&
                Objects.equals(address, employee.address) &&
                Objects.equals(emergencycontact, employee.emergencycontact) &&
                Arrays.equals(image, employee.image) &&
                Objects.equals(doj, employee.doj)
                ;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, number, fullname, nic, mobile, email, address, emergencycontact, doj);
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }
}
