package lk.ashan.routenetlkserverapllication.module.employee.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Collection;
import java.util.Objects;

@Getter
@Entity
public class Employeetype {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "employeetype")
    private Collection<Employee> employees;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employeetype that = (Employeetype) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public void setEmployees(Collection<Employee> employees) {
        this.employees = employees;
    }
}
