package lk.ashan.routenetlkserverapllication.module.farecollection.model.entity;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Objects;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Farecollection",schema = "routenetlk")
public class FareCollection {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "totaltickets")
    private Integer totaltickets;
    @Basic
    @Column(name = "cashcollected")
    private BigDecimal cashcollected;
    @Basic
    @Column(name = "digitalpayments")
    private BigDecimal digitalpayments;
    @Basic
    @Column(name = "isreconciled")
    private Boolean isreconciled;
    @Basic
    @Column(name = "tocollected")
    private Time tocollected;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "tripexecution_id", referencedColumnName = "id", nullable = false)
    private TripExecution tripexecution;
    @ManyToOne
    @JoinColumn(name = "ticketmachine_id", referencedColumnName = "id", nullable = false)
    private TicketMachine ticketmachine;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FareCollection that = (FareCollection) o;
        return Objects.equals(id, that.id) && Objects.equals(totaltickets, that.totaltickets) && Objects.equals(cashcollected, that.cashcollected) && Objects.equals(digitalpayments, that.digitalpayments) && Objects.equals(isreconciled, that.isreconciled) && Objects.equals(tocollected, that.tocollected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totaltickets, cashcollected, digitalpayments, isreconciled, tocollected);
    }

}
