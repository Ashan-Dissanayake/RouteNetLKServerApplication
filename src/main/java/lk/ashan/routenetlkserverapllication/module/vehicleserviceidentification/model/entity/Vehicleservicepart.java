package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity;

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
public class Vehicleservicepart {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "quantity")
    private BigDecimal quantity;
    @ManyToOne
    @JoinColumn(name = "vehicleservice_id", referencedColumnName = "id", nullable = false)
    private Vehicleservice vehicleservice;
    @ManyToOne
    @JoinColumn(name = "part_id", referencedColumnName = "id", nullable = false)
    private Part part;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicleservicepart that = (Vehicleservicepart) o;
        return Objects.equals(id, that.id) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity);
    }
}
