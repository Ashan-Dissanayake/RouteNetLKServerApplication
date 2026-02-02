package lk.ashan.routenetlkserverapllication.module.roster.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Bustype;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Routetypebustype {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "routetype_id", referencedColumnName = "id", nullable = false)
    private Routetype routetype;
    @ManyToOne
    @JoinColumn(name = "bustype_id", referencedColumnName = "id", nullable = false)
    private Bustype bustype;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Routetypebustype that = (Routetypebustype) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
