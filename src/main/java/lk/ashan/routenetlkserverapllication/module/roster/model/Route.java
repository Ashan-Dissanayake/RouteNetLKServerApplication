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
    @Column(name = "source")
    private String source;
    @Basic
    @Column(name = "destination")
    private String destination;

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
                && Objects.equals(source, that.source)
                && Objects.equals(destination, that.destination)
                && Objects.equals(distance, that.distance)
                && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number,source,destination,distance,duration);
    }

}
