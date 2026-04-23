package lk.ashan.routenetlkserverapllication.module.user.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.FareCollection;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleServiceExecution;
import lombok.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "accountlocked")
    private Boolean accountlocked;
    @Basic
    @Column(name = "recoverycode")
    private String recoverycode;
    @Basic
    @Column(name = "recoverycodeexpiration")
    private Timestamp recoverycodeexpiration;
    @Basic
    @Column(name = "recoverycodeused")
    private Boolean recoverycodeused;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "usertype_id", referencedColumnName = "id", nullable = false)
    private UserType usertype;
    @ManyToOne
    @JoinColumn(name = "userstatus_id", referencedColumnName = "id", nullable = false)
    private UserStatus userstatus;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Collection<UserRole> userRoles;

    @OneToMany(mappedBy = "user")
    private Collection<Conductor> conductors;
    @OneToMany(mappedBy = "user")
    private Collection<Driver> drivers;
    @OneToMany(mappedBy = "user")
    private Collection<FareCollection> fareCollections;
    @OneToMany(mappedBy = "user")
    private Collection<Grn> grns;
    @OneToMany(mappedBy = "user")
    private Collection<Incident> incidents;
    @OneToMany(mappedBy = "user")
    private Collection<IncidentVehicleAllocation> incidentvehicleallocations;
    @OneToMany(mappedBy = "user")
    private Collection<Part> parts;
    @OneToMany(mappedBy = "user")
    private Collection<PartRequest> partrequests;
    @OneToMany(mappedBy = "user")
    private Collection<Permite> permites;
    @OneToMany(mappedBy = "user")
    private Collection<Roster> rosters;
    @OneToMany(mappedBy = "user")
    private Collection<Route> routes;
    @OneToMany(mappedBy = "user")
    private Collection<Trip> trips;
    @OneToMany(mappedBy = "user")
    private Collection<TripExecution> tripExecutions;
    @OneToMany(mappedBy = "user")
    private Collection<Vehicle> vehicles;
    @OneToMany(mappedBy = "user")
    private Collection<VehicleService> vehicleServices;
    @OneToMany(mappedBy = "user")
    private Collection<VehicleServiceExecution> vehicleServiceExecutions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(accountlocked, user.accountlocked) && Objects.equals(recoverycode, user.recoverycode) && Objects.equals(recoverycodeexpiration, user.recoverycodeexpiration) && Objects.equals(recoverycodeused, user.recoverycodeused) && Objects.equals(remarks, user.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, accountlocked, recoverycode, recoverycodeexpiration, recoverycodeused, remarks);
    }

}
