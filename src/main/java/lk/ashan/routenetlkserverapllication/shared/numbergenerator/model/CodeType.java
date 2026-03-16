package lk.ashan.routenetlkserverapllication.shared.numbergenerator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "codetype", schema = "routenetlk")
public class CodeType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "codetype")
    private Collection<DocSequence> docSequances;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeType codetype = (CodeType) o;
        return Objects.equals(id, codetype.id) && Objects.equals(name, codetype.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
