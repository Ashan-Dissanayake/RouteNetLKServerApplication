package lk.ashan.routenetlkserverapllication.module.roster.model;

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
public class Shifttype {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "shifttype")
    private Collection<Shift> shifts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shifttype shifttype = (Shifttype) o;
        return Objects.equals(id, shifttype.id) && Objects.equals(name, shifttype.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
