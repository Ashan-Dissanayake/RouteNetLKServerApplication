package lk.ashan.routenetlkserverapllication.module.crew.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;
@Setter
@Getter
@Entity
public class Routefamiliaritylevel {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Routefamiliaritylevel that = (Routefamiliaritylevel) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Collection<Conductor> getConductors() {
        return conductors;
    }

    public void setConductors(Collection<Conductor> conductors) {
        this.conductors = conductors;
    }
}
