package lk.ashan.routenetlkserverapllication.module.grn.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestItem;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "grnpartrequestitem", schema = "routenetlk")
public class GrnPartRequestItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "quantity")
    private BigDecimal quantity;
    @ManyToOne
    @JoinColumn(name = "grn_id", referencedColumnName = "id", nullable = false)
    private Grn grn;
    @ManyToOne
    @JoinColumn(name = "partrequestitem_id", referencedColumnName = "id", nullable = false)
    private PartRequestItem partrequestitem;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrnPartRequestItem that = (GrnPartRequestItem) o;
        return Objects.equals(id, that.id) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity);
    }
}
