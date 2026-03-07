package lk.ashan.routenetlkserverapllication.module.sparepart.model;

import jakarta.persistence.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequestitem;
import lk.ashan.routenetlkserverapllication.shared.model.BaseEntity;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Part extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "sku")
    private String sku;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "photo")
    private byte[] photo;
    @Basic
    @Column(name = "qoh")
    private BigDecimal qoh;
    @Basic
    @Column(name = "maxlevel")
    private BigDecimal maxlevel;
    @Basic
    @Column(name = "rop")
    private BigDecimal rop;
    @Basic
    @Column(name = "dolastordered")
    private LocalDate dolastordered;
    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;
    @ManyToOne
    @JoinColumn(name = "partcategory_id", referencedColumnName = "id", nullable = false)
    private Partcategory partcategory;
    @ManyToOne
    @JoinColumn(name = "unitofmeasure_id", referencedColumnName = "id", nullable = false)
    private Unitofmeasure unitofmeasure;
    @ManyToOne
    @JoinColumn(name = "partstatus_id", referencedColumnName = "id", nullable = false)
    private Partstatus partstatus;

    @OneToMany(mappedBy = "part")
    private Collection<Partrequestitem> partrequestitems;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        return Objects.equals(id, part.id) && Objects.equals(sku, part.sku) && Objects.equals(name, part.name) && Arrays.equals(photo, part.photo) && Objects.equals(qoh, part.qoh) && Objects.equals(maxlevel, part.maxlevel) && Objects.equals(rop, part.rop) && Objects.equals(dolastordered, part.dolastordered);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, sku, name, qoh, maxlevel, rop, dolastordered);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }
}
