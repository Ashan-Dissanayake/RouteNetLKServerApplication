package lk.ashan.routenetlkserverapllication.module.roster.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lombok.*;

import java.time.LocalTime;
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
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "tostart")
    private LocalTime tostart;
    @Basic
    @Column(name = "toend")
    private LocalTime toend;
    @ManyToOne
    @JoinColumn(name = "shiftstatus_id", referencedColumnName = "id", nullable = false)
    private ShiftStatus shiftstatus;

    @OneToMany(mappedBy = "shift")
    private Collection<Trip> trips;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shift shift = (Shift) o;
        return Objects.equals(id, shift.id) && Objects.equals(name, shift.name) && Objects.equals(tostart, shift.tostart) && Objects.equals(toend, shift.toend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tostart, toend);
    }

}
