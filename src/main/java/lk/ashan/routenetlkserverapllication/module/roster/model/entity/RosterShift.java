package lk.ashan.routenetlkserverapllication.module.roster.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Designation;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rostershift", schema = "routenetlk")
public class RosterShift {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "doshift")
    private LocalDate doshift;
    @Basic
    @Column(name = "requiredemployeecount")
    private Integer requiredemployeecount;
    @ManyToOne
    @JoinColumn(name = "shift_id", referencedColumnName = "id", nullable = false)
    private Shift shift;
    @ManyToOne
    @JoinColumn(name = "roster_id", referencedColumnName = "id", nullable = false)
    private Roster roster;

    @ManyToOne
    @JoinColumn(name = "designation_id", referencedColumnName = "id", nullable = false)
    private Designation designation;

    @Transient
    private Integer requiredFamiliarityLevel = 1;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RosterShift that = (RosterShift) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
