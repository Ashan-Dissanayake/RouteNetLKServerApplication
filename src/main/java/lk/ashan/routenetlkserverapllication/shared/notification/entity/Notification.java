package lk.ashan.routenetlkserverapllication.shared.notification.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "message")
    private String message;
    @Basic
    @Column(name = "isread")
    private Boolean isread;
    @Basic
    @Column(name = "tocreated")
    private LocalDateTime tocreated;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(message, that.message) && Objects.equals(isread, that.isread) && Objects.equals(tocreated, that.tocreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, message, isread, tocreated);
    }

}
