package lk.ashan.routenetlkserverapllication.module.branch.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservice;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.model.BaseEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "branch")
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
    private BranchType branchtype;
    @ManyToOne
    @JoinColumn(name = "branchstatus_id", referencedColumnName = "id", nullable = false)
    private BranchStatus branchstatus;
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
    private RegionalOffice regionaloffice;

    @OneToMany(mappedBy = "branch")
    private Collection<Permite> permites;

    @OneToMany(mappedBy = "branch")
    private Collection<Trip> trips;

    @OneToMany(mappedBy = "branch")
    private Collection<Roster> rosters;

    @OneToMany(mappedBy = "branch")
    private Collection<Shift> shifts;

    @OneToMany(mappedBy = "providbranch")
    private Collection<IncidentVehicleAllocation> incidentVehicleAllocations;

    @OneToMany(mappedBy = "branch")
    private Collection<Part> parts;

    @OneToMany(mappedBy = "branch")
    private Collection<PartRequest> partRequests;

    @OneToMany(mappedBy = "branch")
    private Collection<Grn> grns;

    @OneToMany(mappedBy = "branch")
    private Collection<Vehicleservice> vehicleservices;


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
