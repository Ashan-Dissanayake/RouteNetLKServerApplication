package lk.ashan.routenetlkserverapllication.module.fleetallocation.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vehicleavailability {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "doavailability")
    private LocalDate doavailability;
    @Basic
    @Column(name = "inspectionpassed")
    private Boolean inspectionpassed;
    @Basic
    @Column(name = "fulelevelpercentage")
    private Integer fulelevelpercentage;
    @Basic
    @Column(name = "kmonextservice")
    private Integer kmonextservice;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "operatingbranch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "vehicleavailabilitystatus_id", referencedColumnName = "id", nullable = false)
    private Vehicleavailabilitystatus vehicleavailabilitystatus;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicleavailability that = (Vehicleavailability) o;
        return Objects.equals(id, that.id) && Objects.equals(doavailability, that.doavailability) && Objects.equals(inspectionpassed, that.inspectionpassed) && Objects.equals(fulelevelpercentage, that.fulelevelpercentage) && Objects.equals(kmonextservice, that.kmonextservice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doavailability, inspectionpassed, fulelevelpercentage, kmonextservice);
    }

}
