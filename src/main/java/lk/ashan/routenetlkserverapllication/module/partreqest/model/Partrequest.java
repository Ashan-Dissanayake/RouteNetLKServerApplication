package lk.ashan.routenetlkserverapllication.module.partreqest.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Partrequest {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "number")
    private String number;
    @Basic
    @Column(name = "dorequested")
    private LocalDate dorequested;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "partrequeststatus_id", referencedColumnName = "id", nullable = false)
    private Partrequeststatus partrequeststatus;

    @OneToMany(mappedBy = "partrequest")
    private Collection<Partrequestitem> partrequestitems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partrequest that = (Partrequest) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number) && Objects.equals(dorequested, that.dorequested) && Objects.equals(remarks, that.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, dorequested, remarks);
    }

}
