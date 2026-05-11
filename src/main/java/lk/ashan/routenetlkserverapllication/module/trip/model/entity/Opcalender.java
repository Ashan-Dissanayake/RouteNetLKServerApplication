package lk.ashan.routenetlkserverapllication.module.trip.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Opcalender {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "mon")
    private Boolean mon;
    @Basic
    @Column(name = "tue")
    private Boolean tue;
    @Basic
    @Column(name = "wed")
    private Boolean wed;
    @Basic
    @Column(name = "thu")
    private Boolean thu;
    @Basic
    @Column(name = "fri")
    private Boolean fri;
    @Basic
    @Column(name = "sat")
    private Boolean sat;
    @Basic
    @Column(name = "sun")
    private Boolean sun;
    @OneToMany(mappedBy = "opcalender")
    private Collection<Trip> trips;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Opcalender that = (Opcalender) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(mon, that.mon) && Objects.equals(tue, that.tue) && Objects.equals(wed, that.wed) && Objects.equals(thu, that.thu) && Objects.equals(fri, that.fri) && Objects.equals(sat, that.sat) && Objects.equals(sun, that.sun);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mon, tue, wed, thu, fri, sat, sun);
    }

    public boolean isWorkingDay(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY -> Boolean.TRUE.equals(mon);
            case TUESDAY -> Boolean.TRUE.equals(tue);
            case WEDNESDAY -> Boolean.TRUE.equals(wed);
            case THURSDAY -> Boolean.TRUE.equals(thu);
            case FRIDAY -> Boolean.TRUE.equals(fri);
            case SATURDAY -> Boolean.TRUE.equals(sat);
            case SUNDAY -> Boolean.TRUE.equals(sun);
            default -> false;
        };
    }

}
