package lk.ashan.routenetlkserverapllication.module.grn.model;

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
public class Grnstatus {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "grnstatus")
    private Collection<Grn> grns;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grnstatus grnstatus = (Grnstatus) o;
        return Objects.equals(id, grnstatus.id) && Objects.equals(name, grnstatus.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
