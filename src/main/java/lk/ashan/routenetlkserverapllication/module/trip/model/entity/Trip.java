package lk.ashan.routenetlkserverapllication.module.trip.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.Tripcrewallocation;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.Tripcrewattendance;
import lombok.*;

import java.time.LocalDate;
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
    @Column(name = "doservice")
    private LocalDate doservice;
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
    @Column(name = "notrip")
    private Integer notrip;
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
    @OneToMany(mappedBy = "trip")
    private Collection<Tripvehicleoverride> tripvehicleoverrides;
    @ManyToOne
    @JoinColumn(name = "originterminal_id", referencedColumnName = "id", nullable = false)
    private Originterminal originterminal;

    @OneToMany(mappedBy = "trip")
    private Collection<Tripcrewallocation> tripcrewallocations;

    @OneToMany(mappedBy = "trip")
    private Collection<Tripcrewattendance> tripcrewattendances;

    @OneToMany(mappedBy = "trip")
    private Collection<Incident> incidents;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(id, trip.id) && Objects.equals(doservice, trip.doservice) && Objects.equals(todepature, trip.todepature) && Objects.equals(toarrival, trip.toarrival) && Objects.equals(remarks, trip.remarks) && Objects.equals(notrip, trip.notrip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doservice, todepature, toarrival, remarks, notrip);
    }

}
