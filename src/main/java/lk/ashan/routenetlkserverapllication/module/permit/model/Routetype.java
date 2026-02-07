package lk.ashan.routenetlkserverapllication.module.permit.model;

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
public class Routetype {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "routetype")
    private Collection<Route> routes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Routetype routetype = (Routetype) o;
        return Objects.equals(id, routetype.id) && Objects.equals(name, routetype.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
