package lk.ashan.routenetlkserverapllication.module.permit.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Collection;
import java.util.List;
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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "routetype_id", referencedColumnName = "id", nullable = false)
    private RouteType routetype;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "waypoints", columnDefinition = "json")
    private List<Waypoint> waypoints;

    @OneToMany(mappedBy = "route")
    private Collection<RouteBranch> routeBranches;

    @ManyToOne
    @JoinColumn(name = "requiredroutefamiliaritylevel_id", referencedColumnName = "id", nullable = false)
    private RouteFamiliarityLevel requiredroutefamiliaritylevel;

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
