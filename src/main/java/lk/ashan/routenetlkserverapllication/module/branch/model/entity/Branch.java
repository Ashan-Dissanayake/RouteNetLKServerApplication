package lk.ashan.routenetlkserverapllication.module.branch.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.FareCollection;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.TicketMachine;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.RouteBranch;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceExecution;
import lk.ashan.routenetlkserverapllication.shared.model.BaseEntity;
import lk.ashan.routenetlkserverapllication.shared.notification.entity.Notification;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

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
@FilterDef(name = "branchFilter", parameters = {
        @ParamDef(name = "branchId", type = Integer.class),
})
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

    @ManyToOne
    @JoinColumn(name = "regionaloffice_id", referencedColumnName = "id", nullable = false)
    private RegionalOffice regionaloffice;

    @OneToMany(mappedBy = "branch")
    private Collection<Employee> employees;

    @OneToMany(mappedBy = "branch")
    private Collection<Driver> drivers;

    @OneToMany(mappedBy = "branch")
    private Collection<Conductor> conductors;

    @OneToMany(mappedBy = "branch")
    private Collection<Vehicle> vehicles;

    @OneToMany(mappedBy = "branch")
    private Collection<Permite> permites;

    @OneToMany(mappedBy = "branch")
    private Collection<Trip> trips;

    @OneToMany(mappedBy = "branch")
    private Collection<Roster> rosters;

    @OneToMany(mappedBy = "providedbranch")
    private Collection<IncidentVehicleAllocation> incidentVehicleAllocations;

    @OneToMany(mappedBy = "branch")
    private Collection<IncidentVehicleAllocation> incidentVehicleAllocationss;

    @OneToMany(mappedBy = "branch")
    private Collection<Part> parts;

    @OneToMany(mappedBy = "branch")
    private Collection<PartRequest> partRequests;

    @OneToMany(mappedBy = "branch")
    private Collection<Grn> grns;

    @OneToMany(mappedBy = "branch")
    private Collection<VehicleService> vehicleServices;

    @OneToMany(mappedBy = "branch")
    private Collection<RouteBranch> routeBranches;

    @OneToMany(mappedBy = "branch")
    private Collection<TripExecution> tripExecutions;

    @OneToMany(mappedBy = "branch")
    private Collection<VehicleServiceExecution> vehicleServiceExecutions;

    @OneToMany(mappedBy = "branch")
    private Collection<TicketMachine> ticketmachines;

    @OneToMany(mappedBy = "branch")
    private Collection<FareCollection> farecollections;

    @OneToMany(mappedBy = "branch")
    private Collection<Incident> incidents;

    @OneToMany(mappedBy = "branch")
    private Collection<Notification> notifications;



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
