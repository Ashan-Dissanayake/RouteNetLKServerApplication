package lk.ashan.routenetlkserverapllication.module.privilege.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Privilege {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "authority")
    private String authority;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "module_id", referencedColumnName = "id", nullable = false)
    private Module module;
    @ManyToOne
    @JoinColumn(name = "operation_id", referencedColumnName = "id", nullable = false)
    private Operation operation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Privilege privilege = (Privilege) o;
        return Objects.equals(id, privilege.id) && Objects.equals(authority, privilege.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authority);
    }
}
