package lk.ashan.routenetlkserverapllication.module.sparepart.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Partmaster {

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
    @ManyToOne
    @JoinColumn(name = "partcategory_id", referencedColumnName = "id", nullable = false)
    private Partcategory partcategory;
    @ManyToOne
    @JoinColumn(name = "unitofmeasure_id", referencedColumnName = "id", nullable = false)
    private Unitofmeasure unitofmeasure;
    @OneToMany(mappedBy = "partmaster")
    private Collection<Part> parts;
}
