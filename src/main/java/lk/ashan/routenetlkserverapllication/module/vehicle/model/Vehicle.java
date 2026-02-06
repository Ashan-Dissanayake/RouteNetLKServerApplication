package lk.ashan.routenetlkserverapllication.module.vehicle.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.shared.model.BaseEntity;
import lombok.*;


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

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
