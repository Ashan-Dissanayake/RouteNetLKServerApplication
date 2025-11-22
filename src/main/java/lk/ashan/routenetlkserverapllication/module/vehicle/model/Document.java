package lk.ashan.routenetlkserverapllication.module.vehicle.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.Objects;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Document {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "document")
    private byte[] document;
    @Basic
    @Column(name = "version")
    private Integer version;
    @Basic
    @Column(name = "doi")
    private Date doi;
    @Basic
    @Column(name = "doe")
    private Date doe;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "documenttype_id", referencedColumnName = "id", nullable = false)
    private Documenttype documenttype;
    @ManyToOne
    @JoinColumn(name = "documentstatus_id", referencedColumnName = "id", nullable = false)
    private Documentstatus documentstatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document1 = (Document) o;
        return Objects.equals(id, document1.id) && Arrays.equals(document, document1.document) && Objects.equals(version, document1.version) && Objects.equals(doi, document1.doi) && Objects.equals(doe, document1.doe) && Objects.equals(remarks, document1.remarks);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, version, doi, doe, remarks);
        result = 31 * result + Arrays.hashCode(document);
        return result;
    }

}
