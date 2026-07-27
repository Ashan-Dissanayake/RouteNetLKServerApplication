package lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.audit.BranchAnnotationListener;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicleservice",schema = "routenetlk")
//@FilterDef(name = "branchAndUserFilter", parameters = {
//        @ParamDef(name = "branchId", type = Integer.class),
//        @ParamDef(name = "userId", type = Integer.class)
//})
@Filter(name = "branchFilter", condition = "branch_id = :branchId")
@EntityListeners({AuditingEntityListener.class, BranchAnnotationListener.class})
public class VehicleService {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "number")
    private String number;
    @Basic
    @Column(name = "docreated")
    private LocalDate docreated;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "vehicleservicetype_id", referencedColumnName = "id", nullable = false)
    private VehicleServiceType vehicleservicetype;
    @ManyToOne
    @JoinColumn(name = "incident_id", referencedColumnName = "id")
    private Incident incident;
    @ManyToOne
    @JoinColumn(name = "vehicleservicepriority_id", referencedColumnName = "id", nullable = false)
    private VehicleServicePriority vehicleservicepriority;

    @OneToMany(mappedBy = "vehicleservice")
    private Collection<VehicleServicePart> vehicleServiceParts;

    @ManyToOne
    @JoinColumn(name = "vehicleservicestatus_id", referencedColumnName = "id", nullable = false)
    private VehicleServiceStatus vehicleservicestatus;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "vehicleservice", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<VehicleServiceExecution> vehicleServiceExecutions;

    public void addPart(VehicleServicePart servicePart) {
        vehicleServiceParts.add(servicePart);
        servicePart.setVehicleservice(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleService that = (VehicleService) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
