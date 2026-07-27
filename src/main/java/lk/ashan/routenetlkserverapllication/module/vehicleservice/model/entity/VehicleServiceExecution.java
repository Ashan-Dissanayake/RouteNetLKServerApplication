package lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicleserviceexecution",schema = "routenetlk")
//@FilterDef(name = "branchAndUserFilter", parameters = {
//        @ParamDef(name = "branchId", type = Integer.class),
//        @ParamDef(name = "userId", type = Integer.class)
//})
//@Filter(name = "branchAndUserFilter", condition = "branch_id = :branchId AND user_id = :userId")
@EntityListeners({AuditingEntityListener.class})
public class VehicleServiceExecution {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "dostarted")
    private LocalDate dostarted;
    @Basic
    @Column(name = "doend")
    private LocalDate doend;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @Basic
    @Column(name = "startodometer")
    private Integer startodometer;
    @Basic
    @Column(name = "nextserviceinkm")
    private Integer nextserviceinkm;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "vehicleservice_id", referencedColumnName = "id", nullable = false)
    private VehicleService vehicleservice;
    @ManyToOne
    @JoinColumn(name = "maintechnician_id", referencedColumnName = "id", nullable = false)
    private Employee maintechnician;
    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User user;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleServiceExecution that = (VehicleServiceExecution) o;
        return Objects.equals(id, that.id) && Objects.equals(dostarted, that.dostarted) && Objects.equals(doend, that.doend) && Objects.equals(remarks, that.remarks) && Objects.equals(startodometer, that.startodometer) && Objects.equals(nextserviceinkm, that.nextserviceinkm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dostarted, doend, remarks, startodometer, nextserviceinkm);
    }

}
