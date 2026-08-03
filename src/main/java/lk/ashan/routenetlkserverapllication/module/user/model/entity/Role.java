package lk.ashan.routenetlkserverapllication.module.user.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Privilege;
import lk.ashan.routenetlkserverapllication.shared.notification.model.Notification;
import lombok.*;

import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "role",fetch = FetchType.EAGER)
    private Collection<Privilege> privileges;

    @OneToMany(mappedBy = "role")
    private Collection<UserRole> userRoles;

    @OneToMany(mappedBy = "role")
    private Collection<Notification> notifications;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
    
}
