package lk.ashan.routenetlkserverapllication.module.vehicle.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.Incidentvehicleallocation;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservice;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripvehicleoverride;
import lk.ashan.routenetlkserverapllication.shared.model.BaseEntity;
import lombok.*;


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
    private Fueltype fueltype;
    @ManyToOne
    @JoinColumn(name = "conditionrate_id", referencedColumnName = "id", nullable = false)
    private Conditionrate conditionrate;
    @ManyToOne
    @JoinColumn(name = "vehiclestatus_id", referencedColumnName = "id", nullable = false)
    private Vehiclestatus vehiclestatus;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "bustype_id", referencedColumnName = "id", nullable = false)
    private Bustype bustype;
    @ManyToOne
    @JoinColumn(name = "model_id", referencedColumnName = "id", nullable = false)
    private Model model;

    @OneToMany(mappedBy = "vehicle")
    private Collection<Permite> permites;

    @OneToMany(mappedBy = "vehicle")
    private Collection<Tripvehicleoverride> tripvehicleoverrides;

    @OneToMany(mappedBy = "vehicle")
    private Collection<Incidentvehicleallocation> incidentvehicleallocations;

    @OneToMany(mappedBy = "vehicle")
    private Collection<Vehicleservice> vehicleservices;

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
