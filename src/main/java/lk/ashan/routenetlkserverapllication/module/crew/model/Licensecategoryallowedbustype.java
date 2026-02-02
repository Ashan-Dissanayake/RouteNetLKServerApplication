package lk.ashan.routenetlkserverapllication.module.crew.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Bustype;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
@Setter
@Getter
@Entity
public class Licensecategoryallowedbustype {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "licensecategory_id", referencedColumnName = "id", nullable = false)
    private Licensecategory licensecategory;
    @ManyToOne
    @JoinColumn(name = "allowedbustype_id", referencedColumnName = "id", nullable = false)
    private Bustype bustype;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Licensecategoryallowedbustype that = (Licensecategoryallowedbustype) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
