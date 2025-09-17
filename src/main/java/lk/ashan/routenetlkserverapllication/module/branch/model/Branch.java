package lk.ashan.routenetlkserverapllication.module.branch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE branch SET branchstatus_id = 3 WHERE id = ?")
public class Branch {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "code")
    private String code;
    @Basic
    @Column(name = "address")
    private String address;
    @Basic
    @Column(name = "telephone")
    private String telephone;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "docreated")
    private Date docreated;
    @Basic
    @Column(name = "remarks")
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "branchtype_id", referencedColumnName = "id", nullable = false)
    private Branchtype branchtype;
    @ManyToOne
    @JoinColumn(name = "branchstatus_id", referencedColumnName = "id", nullable = false)
    private Branchstatus branchstatus;
    @OneToMany(mappedBy = "branch",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Branchcoverage> branchcoverages;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(id, branch.id) && Objects.equals(name, branch.name) && Objects.equals(code, branch.code) && Objects.equals(address, branch.address) && Objects.equals(telephone, branch.telephone) && Objects.equals(email, branch.email) && Objects.equals(docreated, branch.docreated) && Objects.equals(remarks, branch.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, address, telephone, email, docreated, remarks);
    }

}
