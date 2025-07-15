package lk.ashan.ntcserverapllication.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
public class District {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "district")
    private Collection<Branchcoverage> branchcoverages;
    @ManyToOne
    @JoinColumn(name = "province_id", referencedColumnName = "id", nullable = false)
    private Province province;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        District district = (District) o;
        return Objects.equals(id, district.id) && Objects.equals(name, district.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
