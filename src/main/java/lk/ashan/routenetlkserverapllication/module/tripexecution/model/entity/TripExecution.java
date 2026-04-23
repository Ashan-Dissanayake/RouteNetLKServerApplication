package lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.farecollection.model.entity.FareCollection;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lombok.*;

import java.sql.Date;
import java.sql.Time;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tripexecution",schema = "routenetlk")
public class TripExecution {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "doservice")
    private Date doservice;
    @Basic
    @Column(name = "toactualdeparture")
    private Time toactualdeparture;
    @Basic
    @Column(name = "toactualarrival")
    private Time toactualarrival;
    @Basic
    @Column(name = "startodometer")
    private Integer startodometer;
    @Basic
    @Column(name = "endodometer")
    private Integer endodometer;
    @Basic
    @Column(name = "passengercount")
    private Integer passengercount;
    @Basic
    @Column(name = "tripno")
    private Integer tripno;
    @Basic
    @Column(name = "remarks")
    private String remarks;

    @OneToMany(mappedBy = "tripexecution")
    private Collection<FareCollection> fareCollections;

    @OneToMany(mappedBy = "tripexecution")
    private Collection<Incident> incidents;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "trip_id", referencedColumnName = "id", nullable = false)
    private Trip trip;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;
    @ManyToOne
    @JoinColumn(name = "conductor_id", referencedColumnName = "id")
    private Conductor conductor;
    @ManyToOne
    @JoinColumn(name = "tripexecutionstatus_id", referencedColumnName = "id", nullable = false)
    private TripExecutionStatus tripexecutionstatus;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripExecution that = (TripExecution) o;
        return Objects.equals(id, that.id) && Objects.equals(doservice, that.doservice) && Objects.equals(toactualdeparture, that.toactualdeparture) && Objects.equals(toactualarrival, that.toactualarrival) && Objects.equals(startodometer, that.startodometer) && Objects.equals(endodometer, that.endodometer) && Objects.equals(passengercount, that.passengercount) && Objects.equals(tripno, that.tripno) && Objects.equals(remarks, that.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doservice, toactualdeparture, toactualarrival, startodometer, endodometer, passengercount, tripno, remarks);
    }
}
