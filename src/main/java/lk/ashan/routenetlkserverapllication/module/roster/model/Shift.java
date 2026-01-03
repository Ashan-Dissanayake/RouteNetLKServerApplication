package lk.ashan.routenetlkserverapllication.module.roster.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shift {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "tostarted")
    private Time tostarted;
    @Basic
    @Column(name = "toend")
    private Time toend;
    @OneToMany(mappedBy = "shift")
    private Collection<Roster> rosters;
    @ManyToOne
    @JoinColumn(name = "shifttype_id", referencedColumnName = "id", nullable = false)
    private Shifttype shifttype;
    @ManyToOne
    @JoinColumn(name = "shiftstatus_id", referencedColumnName = "id", nullable = false)
    private Shiftstatus shiftstatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return Objects.equals(id, shift.id) && Objects.equals(tostarted, shift.tostarted) && Objects.equals(toend, shift.toend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tostarted, toend);
    }

}
