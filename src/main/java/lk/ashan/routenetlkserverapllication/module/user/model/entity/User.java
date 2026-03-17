package lk.ashan.routenetlkserverapllication.module.user.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lombok.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "accountlocked")
    private Boolean accountlocked;
    @Basic
    @Column(name = "recoverycode")
    private String recoverycode;
    @Basic
    @Column(name = "recoverycodeexpiration")
    private Timestamp recoverycodeexpiration;
    @Basic
    @Column(name = "recoverycodeused")
    private Boolean recoverycodeused;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "usertype_id", referencedColumnName = "id", nullable = false)
    private UserType usertype;
    @ManyToOne
    @JoinColumn(name = "userstatus_id", referencedColumnName = "id", nullable = false)
    private UserStatus userstatus;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Collection<UserRole> userRoles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(accountlocked, user.accountlocked) && Objects.equals(recoverycode, user.recoverycode) && Objects.equals(recoverycodeexpiration, user.recoverycodeexpiration) && Objects.equals(recoverycodeused, user.recoverycodeused) && Objects.equals(remarks, user.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, accountlocked, recoverycode, recoverycodeexpiration, recoverycodeused, remarks);
    }

}
