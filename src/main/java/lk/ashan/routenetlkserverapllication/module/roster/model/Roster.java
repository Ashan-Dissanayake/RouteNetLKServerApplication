package lk.ashan.routenetlkserverapllication.module.roster.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lombok.*;

import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Roster {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "doroster")
    private Date doroster;
    @ManyToOne
    @JoinColumn(name = "shift_id", referencedColumnName = "id", nullable = false)
    private Shift shift;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @OneToMany(mappedBy = "roster")
    private Collection<Rosterassignement> rosterassignements;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDoroster() {
        return doroster;
    }

    public void setDoroster(Date doroster) {
        this.doroster = doroster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Roster roster = (Roster) o;
        return Objects.equals(id, roster.id) && Objects.equals(doroster, roster.doroster);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doroster);
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Collection<Rosterassignement> getRosterassignements() {
        return rosterassignements;
    }

    public void setRosterassignements(Collection<Rosterassignement> rosterassignements) {
        this.rosterassignements = rosterassignements;
    }
}
