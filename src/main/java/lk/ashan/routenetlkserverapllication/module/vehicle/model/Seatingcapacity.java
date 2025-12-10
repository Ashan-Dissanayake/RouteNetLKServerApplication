package lk.ashan.routenetlkserverapllication.module.vehicle.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lombok.*;

import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Seatingcapacity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "amount")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "make_id", referencedColumnName = "id", nullable = false)
    private Make make;

    @OneToMany(mappedBy = "seatingcapacity")
    private Collection<Vehicle> vehicles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seatingcapacity that = (Seatingcapacity) o;
        return Objects.equals(id, that.id) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount);
    }

}
