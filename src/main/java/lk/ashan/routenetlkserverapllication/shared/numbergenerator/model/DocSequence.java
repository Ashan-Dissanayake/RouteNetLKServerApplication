package lk.ashan.routenetlkserverapllication.shared.numbergenerator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "docsequance",schema = "routenetlk",
        uniqueConstraints = @UniqueConstraint(columnNames = {"codetype_id", "scope_id", "periodkey"}))
public class DocSequence {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "lastvalue")
    private Integer lastvalue;
    @Basic
    @Column(name = "version")
    private Integer version;
    @Basic
    @Column(name = "periodkey")
    private String periodkey;
    @ManyToOne
    @JoinColumn(name = "codetype_id", referencedColumnName = "id", nullable = false)
    private CodeType codetype;
    @ManyToOne
    @JoinColumn(name = "scope_id", referencedColumnName = "id", nullable = false)
    private Scope scope;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocSequence that = (DocSequence) o;
        return Objects.equals(id, that.id) && Objects.equals(lastvalue, that.lastvalue) && Objects.equals(version, that.version) && Objects.equals(periodkey, that.periodkey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lastvalue, version, periodkey);
    }

    public Integer nextValue() {
        lastvalue = lastvalue + 1;   // increment the value
        return lastvalue;            // return the new value
    }
}
