package lk.ashan.routenetlkserverapllication.module.branch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@FilterDef(
//        name = "softDeleteFilter",
//        parameters = @ParamDef(name = "is_deleted", type = Boolean.class)
//)
//@Filters({
//        @Filter(name = "softDeleteFilter", condition = "deleted = :is_deleted")
//})
public class Branch extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "code")
    private String code;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "telephone")
    private String telephone;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "docreated")
    private LocalDate docreated;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "branchtype_id", referencedColumnName = "id", nullable = false)
    private Branchtype branchtype;
    @ManyToOne
    @JoinColumn(name = "branchstatus_id", referencedColumnName = "id", nullable = false)
    private Branchstatus branchstatus;
    @JsonIgnore
    @OneToMany(mappedBy = "branch")
    private Collection<Employee> employees;

//    @Column(name = "deleted")
//    private boolean deleted;

    @JsonIgnore
    @OneToMany(mappedBy = "branch")
    private Collection<Vehicle> vehicles;

    @ManyToOne
    @JoinColumn(name = "regionaloffice_id", referencedColumnName = "id", nullable = false)
    private Regionaloffice regionalofficeByRegionalofficeId;

    @OneToMany(mappedBy = "branch")
    private Collection<Permite> permites;

    @OneToMany(mappedBy = "branch")
    private Collection<Trip> trips;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(id, branch.id) && Objects.equals(name, branch.name) && Objects.equals(code, branch.code) && Objects.equals(address, branch.address) && Objects.equals(telephone, branch.telephone) && Objects.equals(email, branch.email) && Objects.equals(docreated, branch.docreated) && Objects.equals(remarks, branch.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, address, telephone, email, docreated, remarks);
    }

}
