package lk.ashan.routenetlkserverapllication.module.vehicle.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="fueltype",schema = "routenetlk")
public class FuelType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "fueltype")
    private Collection<Vehicle> vehicles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuelType fueltype = (FuelType) o;
        return Objects.equals(id, fueltype.id) && Objects.equals(name, fueltype.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
