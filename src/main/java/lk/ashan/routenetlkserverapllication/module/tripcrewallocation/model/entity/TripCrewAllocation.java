package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lombok.*;

import java.time.LocalTime;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tripcrewallocation", schema = "routenetlk")
public class TripCrewAllocation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @Basic
    @Column(name = "toallocated")
    private LocalTime toallocated;
    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "tripcrewallocationstatus_id", referencedColumnName = "id", nullable = false)
    private TripCrewAllocationStatus tripcrewallocationstatus;

    @ManyToOne
    @JoinColumn(name = "rostershiftassignment_id", referencedColumnName = "id", nullable = false)
    private RosterShiftAssignment rostershiftassignment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripCrewAllocation that = (TripCrewAllocation) o;
        return Objects.equals(id, that.id) && Objects.equals(remarks, that.remarks);
    }

}
