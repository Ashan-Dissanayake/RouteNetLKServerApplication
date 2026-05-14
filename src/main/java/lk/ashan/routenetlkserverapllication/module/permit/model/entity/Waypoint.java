package lk.ashan.routenetlkserverapllication.module.permit.model.entity;


import lombok.Data;

@Data
public class Waypoint {
    private Integer order;
    private String location;
    private Double lat;
    private Double lng;
}
