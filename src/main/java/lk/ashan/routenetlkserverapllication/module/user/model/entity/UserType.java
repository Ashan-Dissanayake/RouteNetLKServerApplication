package lk.ashan.routenetlkserverapllication.module.user.model.entity;

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
@Table(name = "usertype",schema = "routenetlk")
public class UserType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "usertype")
    private Collection<User> users;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserType usertype = (UserType) o;
        return Objects.equals(id, usertype.id) && Objects.equals(name, usertype.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
