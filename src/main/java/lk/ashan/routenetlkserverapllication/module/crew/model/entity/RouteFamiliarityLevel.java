package lk.ashan.routenetlkserverapllication.module.crew.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "routefamiliaritylevel", schema = "routenetlk")
public class RouteFamiliarityLevel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "routefamiliaritylevel")
    private Collection<Driver> drivers;

    @OneToMany(mappedBy = "routefamiliaritylevel")
    private Collection<Conductor> conductors;

    @OneToMany(mappedBy = "requiredroutefamiliaritylevel")
    private Collection<Route> routes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteFamiliarityLevel that = (RouteFamiliarityLevel) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
