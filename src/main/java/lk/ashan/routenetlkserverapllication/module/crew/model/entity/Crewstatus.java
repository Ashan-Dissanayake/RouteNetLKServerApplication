package lk.ashan.routenetlkserverapllication.module.crew.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Crewstatus {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "crewstatus")
    private Collection<Driver> drivers;
    @OneToMany(mappedBy = "crewstatus")
    private Collection<Conductor> conductors;

    public Crewstatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crewstatus that = (Crewstatus) o;
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
