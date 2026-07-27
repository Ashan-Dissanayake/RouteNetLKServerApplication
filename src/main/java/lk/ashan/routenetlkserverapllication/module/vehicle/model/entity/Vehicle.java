package lk.ashan.routenetlkserverapllication.module.vehicle.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.shared.audit.BranchAnnotationListener;
import lk.ashan.routenetlkserverapllication.shared.audit.CurrentBranch;
import lk.ashan.routenetlkserverapllication.shared.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@FilterDef(name = "branchAndUserFilter", parameters = {
//        @ParamDef(name = "branchId", type = Integer.class),
//        @ParamDef(name = "userId", type = Integer.class)
//})
@Filter(name = "branchFilter", condition = "branch_id = :branchId")
@EntityListeners({AuditingEntityListener.class, BranchAnnotationListener.class})
public class Vehicle extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "number")
    private String number;
    @Basic
    @Column(name = "mileage")
    private Integer mileage;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "fueltype_id", referencedColumnName = "id", nullable = false)
    private FuelType fueltype;
    @ManyToOne
    @JoinColumn(name = "conditionrate_id", referencedColumnName = "id", nullable = false)
    private ConditionRate conditionrate;
    @ManyToOne
    @JoinColumn(name = "vehiclestatus_id", referencedColumnName = "id", nullable = false)
    private VehicleStatus vehiclestatus;
    @CurrentBranch
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "bustype_id", referencedColumnName = "id", nullable = false)
    private BusType bustype;
    @ManyToOne
    @JoinColumn(name = "model_id", referencedColumnName = "id", nullable = false)
    private Model model;
    @OneToMany(mappedBy = "vehicle")
    private Collection<Permite> permites;
    @OneToMany(mappedBy = "vehicle")
    private Collection<IncidentVehicleAllocation> incidentVehicleAllocations;
    @OneToMany(mappedBy = "vehicle")
    private Collection<VehicleService> vehicleServices;
    @OneToMany(mappedBy = "vehicle")
    private Collection<TripExecution> tripExecutions;
    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User user;

    @Basic
    @Column(name = "deleted")
    private Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id)&& Objects.equals(number, vehicle.number)&& Objects.equals(mileage, vehicle.mileage) && Objects.equals(remarks, vehicle.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number,mileage, remarks);
    }


}
