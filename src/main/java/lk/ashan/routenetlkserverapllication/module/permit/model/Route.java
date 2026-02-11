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
public class Route {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "number")
    private String number;
    @Basic
    @Column(name = "origin")
    private String origin;
    @Basic
    @Column(name = "destination")
    private String destination;
    @Basic
    @Column(name = "distancekm")
    private Integer distancekm;
    @Basic
    @Column(name = "mingapminutes")
    private Integer mingapminutes;

    @OneToMany(mappedBy = "route")
    private Collection<Permite> permites;
    @ManyToOne
    @JoinColumn(name = "scheduletype_id", referencedColumnName = "id", nullable = false)
    private Scheduletype scheduletype;
    @ManyToOne
    @JoinColumn(name = "routetype_id", referencedColumnName = "id", nullable = false)
    private Routetype routetype;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(id, route.id) && Objects.equals(number, route.number) && Objects.equals(distancekm, route.distancekm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, distancekm);
    }
}
