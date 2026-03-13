package lk.ashan.routenetlkserverapllication.module.trip.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Triptype {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "triptype")
    private Collection<Trip> trips;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triptype triptype = (Triptype) o;
        return Objects.equals(id, triptype.id) && Objects.equals(name, triptype.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
