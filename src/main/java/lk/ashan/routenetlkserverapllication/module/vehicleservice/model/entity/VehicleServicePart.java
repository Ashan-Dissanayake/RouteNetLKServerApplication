package lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Vehicleservicepart",schema = "routenetlk")
public class VehicleServicePart {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "quantity")
    private BigDecimal quantity;
    @ManyToOne
    @JoinColumn(name = "vehicleservice_id", referencedColumnName = "id", nullable = false)
    private VehicleService vehicleservice;
    @ManyToOne
    @JoinColumn(name = "part_id", referencedColumnName = "id", nullable = false)
    private Part part;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleServicePart that = (VehicleServicePart) o;
        return Objects.equals(id, that.id) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity);
    }
}
