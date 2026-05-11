package lk.ashan.routenetlkserverapllication.module.trip.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
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
public class Trip {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "todepature")
    private LocalTime todepature;
    @Basic
    @Column(name = "toarrival")
    private LocalTime toarrival;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @Basic
    @Column(name = "breakminutes")
    private Integer breakminutes;
    @ManyToOne
    @JoinColumn(name = "triptype_id", referencedColumnName = "id", nullable = false)
    private Triptype triptype;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "permite_id", referencedColumnName = "id", nullable = false)
    private Permite permite;
    @ManyToOne
    @JoinColumn(name = "tripstatus_id", referencedColumnName = "id", nullable = false)
    private Tripstatus tripstatus;

    @ManyToOne
    @JoinColumn(name = "originterminal_id", referencedColumnName = "id", nullable = false)
    private Originterminal originterminal;

    @OneToMany(mappedBy = "trip")
    private Collection<TripExecution> tripExecutions;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "opcalender_id", referencedColumnName = "id", nullable = false)
    private Opcalender opcalender;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(id, trip.id) && Objects.equals(todepature, trip.todepature) && Objects.equals(toarrival, trip.toarrival) && Objects.equals(remarks, trip.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, todepature, toarrival, remarks);
    }
}
