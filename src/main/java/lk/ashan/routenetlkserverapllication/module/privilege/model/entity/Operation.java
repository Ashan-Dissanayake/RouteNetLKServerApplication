package lk.ashan.routenetlkserverapllication.module.privilege.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Operation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "displayname")
    private String displayname;
    @Basic
    @Column(name = "operation")
    private String operation;
    @ManyToOne
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    private Module module;
    @OneToMany(mappedBy = "operation")
    private Collection<Privilege> privileges;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation1 = (Operation) o;
        return Objects.equals(id, operation1.id) && Objects.equals(displayname, operation1.displayname) && Objects.equals(operation, operation1.operation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayname, operation);
    }

}
