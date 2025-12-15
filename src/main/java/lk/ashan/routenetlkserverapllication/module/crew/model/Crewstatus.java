package lk.ashan.routenetlkserverapllication.module.crew.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Objects;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Crewstatus {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "crewstatus")
    private Collection<Conductor> conductors;
    @OneToMany(mappedBy = "crewstatus")
    private Collection<Driver> drivers;

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

}
