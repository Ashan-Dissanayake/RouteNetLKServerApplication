package lk.ashan.routenetlkserverapllication.module.trip.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.shared.audit.BranchAnnotationListener;
import lk.ashan.routenetlkserverapllication.shared.audit.CurrentBranch;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a Trip entity with details such as departure and arrival times,
 * associated branch, permit, trip type, and other related information.
 * This entity is mapped to a database table using JPA annotations.
 */
@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Filter(name = "branchFilter", condition = "branch_id = :branchId")
@EntityListeners({AuditingEntityListener.class, BranchAnnotationListener.class})
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

    @CurrentBranch
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

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "opcalender_id", referencedColumnName = "id", nullable = false)
    private Opcalender opcalender;

    @ManyToOne
    @JoinColumn(name = "shift_id", referencedColumnName = "id", nullable = false)
    private Shift shift;

    /**
     * Checks if this Trip is equal to another object.
     *
     * @param o the object to compare with this Trip
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(id, trip.id) && Objects.equals(todepature, trip.todepature) && Objects.equals(toarrival, trip.toarrival) && Objects.equals(remarks, trip.remarks);
    }

    /**
     * Computes the hash code for this Trip.
     *
     * @return the hash code value for this Trip
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, todepature, toarrival, remarks);
    }
}
