package lk.ashan.routenetlkserverapllication.module.roster.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "number")
    private String number;
    @Basic
    @Column(name = "from")
    private String from;
    @Basic
    @Column(name = "to")
    private String to;

    @Basic
    @Column(name = "distance")
    private Integer distance;

    @Basic
    @Column(name = "duration")
    private Integer duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route that = (Route) o;
        return Objects.equals(id, that.id)
                && Objects.equals(number, that.number)
                && Objects.equals(from, that.from)
                && Objects.equals(to, that.to)
                && Objects.equals(distance, that.distance)
                && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number,from,to,distance,duration);
    }

}
